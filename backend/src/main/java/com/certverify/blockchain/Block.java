package com.certverify.blockchain;
import com.certverify.util.HashUtil;
import lombok.Data;

/**
 * Block - represents a single block in our simulated blockchain.
 * 
 * Blockchain concept:
 * - Each block stores certificate data + hash of PREVIOUS block
 * - This creates a chain - if you change any block, ALL following hashes change
 * - This makes tampering detectable!
 * 
 * Real blockchain (Ethereum) uses this same concept but distributed across nodes.
 */
@Data
public class Block {
    private int index;           // Block number in chain
    private String certificateId;
    private String dataHash;     // Hash of certificate data
    private String previousHash; // Hash of previous block (the chain link!)
    private long timestamp;
    private int nonce;
    private String blockHash;    // Hash of everything in this block

    public Block(int index, String certificateId, String dataHash, String previousHash) {
        this.index = index;
        this.certificateId = certificateId;
        this.dataHash = dataHash;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.nonce = 0;
        this.blockHash = calculateHash();
    }

    /**
     * Calculate hash of this block.
     * Combines: index + certificateId + dataHash + previousHash + timestamp + nonce
     */
    public String calculateHash() {
        String combined = index + certificateId + dataHash + previousHash + timestamp + nonce;
        return HashUtil.sha256(combined);
    }
}
