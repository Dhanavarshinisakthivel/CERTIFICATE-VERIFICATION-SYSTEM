package com.certverify.dto;
import lombok.Data;
import lombok.AllArgsConstructor;

/** What we send back after successful login */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;       // JWT token for future requests
    private String username;
    private String fullName;
    private String message;
}
