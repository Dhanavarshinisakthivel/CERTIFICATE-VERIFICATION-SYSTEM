package com.certverify.repository;
import com.certverify.entity.VerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/** Repository for verification logs */
@Repository
public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {
    List<VerificationLog> findTop20ByOrderByVerifiedAtDesc();
    long countByResult(VerificationLog.VerificationResult result);
}
