package com.certverify.service;
import com.certverify.dto.LoginRequest;
import com.certverify.dto.LoginResponse;
import com.certverify.entity.Admin;
import com.certverify.repository.AdminRepository;
import com.certverify.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService - handles admin login and authentication.
 */
@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Login: check username + password, return JWT token if valid.
     */
    public LoginResponse login(LoginRequest request) {
        // Find admin by username
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Check if password matches (BCrypt comparison)
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(admin.getUsername());

        return new LoginResponse(token, admin.getUsername(), admin.getFullName(), "Login successful");
    }
}
