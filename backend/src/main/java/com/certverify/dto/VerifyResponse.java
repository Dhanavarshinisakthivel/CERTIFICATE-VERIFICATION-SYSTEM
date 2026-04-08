package com.certverify.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/** Result of a verification request */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponse {
    private boolean valid;              // true = real, false = fake
    private String status;             // "VALID" or "INVALID" or "NOT_FOUND"
    private String message;            // Human-readable message
    private CertificateResponse certificate;  // full cert details if found
    private String blockHash;          // blockchain block hash
    private boolean blockchainVerified; // did blockchain verify it?
}
