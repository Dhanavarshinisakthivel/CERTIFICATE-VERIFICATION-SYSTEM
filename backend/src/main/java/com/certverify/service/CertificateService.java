package com.certverify.service;
import com.certverify.blockchain.BlockchainService;
import com.certverify.dto.*;
import com.certverify.entity.Certificate;
import com.certverify.entity.VerificationLog;
import com.certverify.repository.CertificateRepository;
import com.certverify.repository.VerificationLogRepository;
import com.certverify.util.HashUtil;
import com.certverify.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CertificateService - contains all business logic for certificates.
 * 
 * Business logic = the actual rules of how certificates work.
 * Controllers call services, services call repositories.
 */
@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certRepo;

    @Autowired
    private VerificationLogRepository logRepo;

    @Autowired
    private BlockchainService blockchainService;

    // Frontend URL for QR codes (change in production)
    @Value("${frontend.url:http://localhost:5500}")
    private String frontendUrl;

    /**
     * Issue (create) a new certificate.
     * Steps: validate -> generate ID -> hash -> blockchain -> QR -> save
     */
    public CertificateResponse issueCertificate(CertificateRequest request, String issuedBy) {
        // Step 1: Generate a unique certificate ID
        long totalCerts = certRepo.count();
        String certId = HashUtil.generateCertificateId(totalCerts);

        // Make sure ID is unique (just in case)
        while (certRepo.existsByCertificateId(certId)) {
            totalCerts++;
            certId = HashUtil.generateCertificateId(totalCerts);
        }

        // Step 2: Generate hash from certificate data (blockchain security)
        String dataHash = HashUtil.generateCertificateHash(
                certId,
                request.getStudentName(),
                request.getCourseName(),
                request.getIssueDate().toString(),
                request.getGrade(),
                request.getInstituteName()
        );

        // Step 3: Add to blockchain simulation
        blockchainService.addBlock(certId, dataHash);

        // Step 4: Generate QR code
        String qrCode = QRCodeUtil.generateQRCode(certId, frontendUrl);

        // Step 5: Save to database
        Certificate cert = new Certificate();
        cert.setCertificateId(certId);
        cert.setStudentName(request.getStudentName());
        cert.setCourseName(request.getCourseName());
        cert.setIssueDate(request.getIssueDate());
        cert.setGrade(request.getGrade());
        cert.setInstituteName(request.getInstituteName());
        cert.setHashValue(dataHash);
        cert.setQrCodeData(qrCode);
        cert.setIssuedBy(issuedBy);
        cert.setIsValid(true);

        Certificate saved = certRepo.save(cert);
        System.out.println("Certificate issued: " + certId);

        return mapToResponse(saved);
    }

    /**
     * Get all certificates (admin view)
     */
    public List<CertificateResponse> getAllCertificates() {
        return certRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get one certificate by its ID
     */
    public Optional<CertificateResponse> getCertificateById(String certId) {
        return certRepo.findByCertificateId(certId)
                .map(this::mapToResponse);
    }

    /**
     * Search certificates by student name
     */
    public List<CertificateResponse> searchByStudentName(String name) {
        return certRepo.findByStudentNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Verify a certificate - the main verification logic.
     * Checks: exists in DB? valid? hash matches blockchain?
     */
    public VerifyResponse verifyCertificate(String certId, String ipAddress) {
        // Step 1: Find certificate in database
        Optional<Certificate> certOpt = certRepo.findByCertificateId(certId);

        if (certOpt.isEmpty()) {
            // Not found - log it and return invalid
            logVerification(certId, ipAddress, VerificationLog.VerificationResult.NOT_FOUND);
            return new VerifyResponse(false, "NOT_FOUND",
                    "Certificate not found. It may be fake or the ID is wrong.", null, null, false);
        }

        Certificate cert = certOpt.get();

        // Step 2: Check if admin has revoked this certificate
        if (!cert.getIsValid()) {
            logVerification(certId, ipAddress, VerificationLog.VerificationResult.INVALID);
            return new VerifyResponse(false, "INVALID",
                    "This certificate has been revoked by the institute.", mapToResponse(cert), null, false);
        }

        // Step 3: Regenerate hash from current DB data and compare with stored hash
        String regeneratedHash = HashUtil.generateCertificateHash(
                cert.getCertificateId(),
                cert.getStudentName(),
                cert.getCourseName(),
                cert.getIssueDate().toString(),
                cert.getGrade(),
                cert.getInstituteName()
        );

        // Step 4: Verify against blockchain
        String blockHash = blockchainService.verifyOnBlockchain(certId, regeneratedHash);
        boolean blockchainVerified = blockHash != null;

        // Step 5: Check if data has been tampered with
        if (!regeneratedHash.equals(cert.getHashValue()) || !blockchainVerified) {
            logVerification(certId, ipAddress, VerificationLog.VerificationResult.INVALID);
            return new VerifyResponse(false, "INVALID",
                    "WARNING: Certificate data may have been tampered with!", mapToResponse(cert), blockHash, false);
        }

        // All checks passed - certificate is VALID
        logVerification(certId, ipAddress, VerificationLog.VerificationResult.VALID);
        return new VerifyResponse(true, "VALID",
                "Certificate is authentic and verified!", mapToResponse(cert), blockHash, true);
    }

    /**
     * Revoke a certificate (admin can invalidate a cert)
     */
    public void revokeCertificate(String certId) {
        Certificate cert = certRepo.findByCertificateId(certId)
                .orElseThrow(() -> new RuntimeException("Certificate not found: " + certId));
        cert.setIsValid(false);
        certRepo.save(cert);
    }

    /**
     * Dashboard stats for admin
     */
    public java.util.Map<String, Long> getDashboardStats() {
        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("total", certRepo.count());
        stats.put("valid", certRepo.countByIsValid(true));
        stats.put("revoked", certRepo.countByIsValid(false));
        stats.put("verifications", logRepo.count());
        stats.put("successfulVerifications", logRepo.countByResult(VerificationLog.VerificationResult.VALID));
        return stats;
    }

    /** Save a verification attempt to the log */
    private void logVerification(String certId, String ip, VerificationLog.VerificationResult result) {
        VerificationLog log = new VerificationLog();
        log.setCertificateId(certId);
        log.setVerifiedByIp(ip);
        log.setResult(result);
        logRepo.save(log);
    }

    /** Convert Certificate entity to CertificateResponse DTO */
    private CertificateResponse mapToResponse(Certificate cert) {
        return new CertificateResponse(
                cert.getId(),
                cert.getCertificateId(),
                cert.getStudentName(),
                cert.getCourseName(),
                cert.getIssueDate(),
                cert.getGrade(),
                cert.getInstituteName(),
                cert.getHashValue(),
                cert.getQrCodeData(),
                cert.getIsValid(),
                cert.getIssuedBy(),
                cert.getCreatedAt()
        );
    }
}
