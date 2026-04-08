package com.certverify.blockchain;
import com.certverify.entity.BlockchainBlock;
import com.certverify.repository.BlockchainBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * BlockchainService - simulates a blockchain for certificate storage.
 * 
 * How it works:
 * 1. First certificate creates Block 0 (genesis block) with previousHash = "0"
 * 2. Each new certificate creates a new block with previousHash = previous block's hash
 * 3. This creates a chain that cannot be tampered with
 * 
 * To connect to real Ethereum: replace addBlock() with a Web3j call to a Solidity contract.
 */
@Service
public class BlockchainService {

    @Autowired
    private BlockchainBlockRepository blockchainBlockRepository;

    /**
     * Add a new block to the blockchain for a certificate.
     * Returns the new block's hash.
     */
    public String addBlock(String certificateId, String dataHash) {
        // Get the last block in the chain
        Optional<BlockchainBlock> lastBlockOpt = blockchainBlockRepository
                .findTopByOrderByBlockIndexDesc();

        // If no blocks exist, use "0" as the genesis previous hash
        String previousHash = lastBlockOpt.map(BlockchainBlock::getBlockHash).orElse("0");
        int newIndex = lastBlockOpt.map(b -> b.getBlockIndex() + 1).orElse(0);

        // Create the new block
        Block newBlock = new Block(newIndex, certificateId, dataHash, previousHash);

        // Save to database
        BlockchainBlock blockEntity = new BlockchainBlock();
        blockEntity.setBlockIndex(newBlock.getIndex());
        blockEntity.setCertificateId(newBlock.getCertificateId());
        blockEntity.setDataHash(newBlock.getDataHash());
        blockEntity.setPreviousHash(newBlock.getPreviousHash());
        blockEntity.setBlockHash(newBlock.getBlockHash());
        blockEntity.setTimestampVal(newBlock.getTimestamp());
        blockEntity.setNonce(newBlock.getNonce());

        blockchainBlockRepository.save(blockEntity);

        System.out.println("Block #" + newIndex + " added. Hash: " + newBlock.getBlockHash());
        return newBlock.getBlockHash();
    }

    /**
     * Verify that a certificate's hash matches what's in the blockchain.
     * Returns the block hash if found, null if not found or tampered.
     */
    public String verifyOnBlockchain(String certificateId, String expectedDataHash) {
        Optional<BlockchainBlock> blockOpt = blockchainBlockRepository
                .findByCertificateId(certificateId);

        if (blockOpt.isEmpty()) {
            return null; // Not found in blockchain
        }

        BlockchainBlock block = blockOpt.get();

        // Check if stored data hash matches the regenerated hash
        if (block.getDataHash().equals(expectedDataHash)) {
            return block.getBlockHash(); // Verified!
        }

        return null; // Hash mismatch = tampered!
    }
}
