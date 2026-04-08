package com.certverify;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CertVerifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CertVerifyApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Blockchain Certificate Verification System");
        System.out.println("  Backend: http://localhost:8080");
        System.out.println("  API: http://localhost:8080/api");
        System.out.println("==============================================");
    }
}
