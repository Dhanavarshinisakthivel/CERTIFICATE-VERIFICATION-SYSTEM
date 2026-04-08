package com.certverify.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/** DTO = Data Transfer Object - what the frontend sends to create a certificate */
@Data
public class CertificateRequest {

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotBlank(message = "Grade is required")
    private String grade;

    @NotBlank(message = "Institute name is required")
    private String instituteName;
}
