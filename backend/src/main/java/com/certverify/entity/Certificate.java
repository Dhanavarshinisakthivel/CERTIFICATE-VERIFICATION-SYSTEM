package com.certverify.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Certificate Entity - maps to certificates table in MySQL */
@Entity
@Table(name = "certificates")
@Data @NoArgsConstructor @AllArgsConstructor
public class Certificate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_id", unique = true, nullable = false)
    private String certificateId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "institute_name", nullable = false)
    private String instituteName;

    @Column(name = "hash_value", nullable = false)
    private String hashValue;

    @Column(name = "qr_code_data", columnDefinition = "TEXT")
    private String qrCodeData;

    @Column(name = "is_valid")
    private Boolean isValid = true;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
