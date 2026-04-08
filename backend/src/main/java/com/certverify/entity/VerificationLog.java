package com.certverify.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/** Audit log for every verification attempt */
@Entity
@Table(name = "verification_logs")
@Data @NoArgsConstructor
public class VerificationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_id")
    private String certificateId;

    @Column(name = "verified_by_ip")
    private String verifiedByIp;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private VerificationResult result;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    public enum VerificationResult { VALID, INVALID, NOT_FOUND }

    @PrePersist
    protected void onCreate() { verifiedAt = LocalDateTime.now(); }
}
