package com.certverify.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * HashUtil - Generates SHA-256 hashes for certificate data.
 * This is the core of our blockchain simulation.
 * SHA-256 always produces the same hash for the same input.
 * If certificate data changes even by 1 character, hash completely changes.
 */
public class HashUtil {

    /**
     * Generates a SHA-256 hash from certificate fields.
     * We combine all fields into one string, then hash it.
     */
    public static String generateCertificateHash(
            String certificateId,
            String studentName,
            String courseName,
            String issueDate,
            String grade,
            String instituteName) {
        // Combine all fields into one string
        String combined = certificateId + "|" + studentName + "|" +
                          courseName + "|" + issueDate + "|" +
                          grade + "|" + instituteName;
        return sha256(combined);
    }

    /**
     * Core SHA-256 hashing function.
     * Takes any string and returns a 64-character hex hash.
     */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Generates a unique certificate ID like CERT-2024-0001
     */
    public static String generateCertificateId(long count) {
        int year = java.time.LocalDate.now().getYear();
        return String.format("CERT-%d-%04d", year, count + 1);
    }
}
