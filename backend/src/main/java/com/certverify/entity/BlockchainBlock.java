package com.certverify.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/** One block in our simulated blockchain */
@Entity
@Table(name = "blockchain_blocks")
@Data @NoArgsConstructor @AllArgsConstructor
public class BlockchainBlock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_index", nullable = false)
    private Integer blockIndex;

    @Column(name = "certificate_id", nullable = false)
    private String certificateId;

    @Column(name = "data_hash", nullable = false)
    private String dataHash;

    @Column(name = "previous_hash", nullable = false)
    private String previousHash;

    @Column(name = "block_hash", nullable = false)
    private String blockHash;

    @Column(name = "timestamp_val", nullable = false)
    private Long timestampVal;

    @Column(name = "nonce")
    private Integer nonce = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
