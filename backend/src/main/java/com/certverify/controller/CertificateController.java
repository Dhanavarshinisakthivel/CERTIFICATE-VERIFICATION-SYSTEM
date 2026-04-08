package com.certverify.controller;
import com.certverify.dto.*;
import com.certverify.service.CertificateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CertificateController - handles all /api/certificates/* endpoints.
 * 
 * PUBLIC (no login needed):
 *   GET  /api/certificates/verify/{id}      -> Verify a certificate
 * 
 * PROTECTED (admin must be logged in):
 *   POST /api/certificates/issue            -> Issue new certificate
 *   GET  /api/certificates                  -> Get all certificates
 *   GET  /api/certificates/{id}             -> Get one certificate
 *   GET  /api/certificates/search?name=X    -> Search by student name
 *   PUT  /api/certificates/{id}/revoke      -> Revoke a certificate
 *   GET  /api/certificates/stats            -> Dashboard statistics
 */
@RestController
@RequestMapping("/api/certificates")
@CrossOrigin(origins = "*")
public class CertificateController {

    @Autowired
    private CertificateService certService;

    // ===================== PUBLIC ENDPOINTS =====================

    /** Verify certificate - public endpoint, anyone can use */
    @GetMapping("/verify/{certId}")
    public ResponseEntity<ApiResponse<VerifyResponse>> verify(
            @PathVariable String certId,
            HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        VerifyResponse result = certService.verifyCertificate(certId, ip);

        if (result.isValid()) {
            return ResponseEntity.ok(ApiResponse.ok("Verification complete", result));
        } else {
            return ResponseEntity.ok(ApiResponse.error(result.getMessage()));
        }
    }

    // ===================== PROTECTED (ADMIN) ENDPOINTS =====================

    /** Issue a new certificate */
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<CertificateResponse>> issueCertificate(
            @Valid @RequestBody CertificateRequest request,
            Authentication auth) {

        try {
            String issuedBy = auth != null ? auth.getName() : "admin";
            CertificateResponse response = certService.issueCertificate(request, issuedBy);
            return ResponseEntity.ok(ApiResponse.ok("Certificate issued successfully! ID: " + response.getCertificateId(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to issue certificate: " + e.getMessage()));
        }
    }

    /** Get all certificates */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getAllCertificates() {
        List<CertificateResponse> certs = certService.getAllCertificates();
        return ResponseEntity.ok(ApiResponse.ok("Fetched " + certs.size() + " certificates", certs));
    }

    /** Get certificate by ID */
    @GetMapping("/{certId}")
    public ResponseEntity<ApiResponse<CertificateResponse>> getCertificate(@PathVariable String certId) {
        Optional<CertificateResponse> cert = certService.getCertificateById(certId);
        return cert.map(c -> ResponseEntity.ok(ApiResponse.ok("Certificate found", c)))
                   .orElse(ResponseEntity.notFound().build());
    }

    /** Search certificates by student name */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> search(@RequestParam String name) {
        List<CertificateResponse> results = certService.searchByStudentName(name);
        return ResponseEntity.ok(ApiResponse.ok("Found " + results.size() + " results", results));
    }

    /** Revoke (invalidate) a certificate */
    @PutMapping("/{certId}/revoke")
    public ResponseEntity<ApiResponse<Void>> revoke(@PathVariable String certId) {
        try {
            certService.revokeCertificate(certId);
            return ResponseEntity.ok(ApiResponse.ok("Certificate revoked successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** Dashboard statistics for admin */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = certService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.ok("Stats fetched", stats));
    }
}
