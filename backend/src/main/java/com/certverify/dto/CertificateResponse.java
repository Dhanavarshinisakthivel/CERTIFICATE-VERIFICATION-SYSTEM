package com.certverify.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** What we send back to the frontend after certificate operations */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {
    private Long id;
    private String certificateId;
    private String studentName;
    private String courseName;
    private LocalDate issueDate;
    private String grade;
    private String instituteName;
    private String hashValue;
    private String qrCodeData;
    private Boolean isValid;
    private String issuedBy;
    private LocalDateTime createdAt;
}
