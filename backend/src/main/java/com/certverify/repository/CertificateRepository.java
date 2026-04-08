package com.certverify.repository;
import com.certverify.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/** Repository layer - handles all database queries for certificates */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    // Find by certificate ID (e.g., CERT-2024-0001)
    Optional<Certificate> findByCertificateId(String certificateId);

    // Find by student name (case-insensitive search)
    List<Certificate> findByStudentNameContainingIgnoreCase(String studentName);

    // Find by institute name
    List<Certificate> findByInstituteNameContainingIgnoreCase(String instituteName);

    // Find all valid certificates
    List<Certificate> findByIsValid(Boolean isValid);

    // Count total certificates
    long countByIsValid(Boolean isValid);

    // Check if certificate ID already exists
    boolean existsByCertificateId(String certificateId);
}
