package com.certverify.repository;
import com.certverify.entity.BlockchainBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/** Repository for blockchain block queries */
@Repository
public interface BlockchainBlockRepository extends JpaRepository<BlockchainBlock, Long> {
    Optional<BlockchainBlock> findByCertificateId(String certificateId);
    Optional<BlockchainBlock> findTopByOrderByBlockIndexDesc();
}
