-- =====================================================
-- Blockchain-Based Certificate Verification System
-- MySQL Database Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS cert_verify_db;
USE cert_verify_db;

-- Admin table for login
CREATE TABLE IF NOT EXISTS admins (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    full_name   VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Main certificates table
CREATE TABLE IF NOT EXISTS certificates (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    certificate_id  VARCHAR(50) NOT NULL UNIQUE,
    student_name    VARCHAR(100) NOT NULL,
    course_name     VARCHAR(150) NOT NULL,
    issue_date      DATE NOT NULL,
    grade           VARCHAR(20) NOT NULL,
    institute_name  VARCHAR(150) NOT NULL,
    hash_value      VARCHAR(256) NOT NULL,
    qr_code_data    TEXT,
    is_valid        BOOLEAN DEFAULT TRUE,
    issued_by       VARCHAR(100),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Simulated blockchain blocks
CREATE TABLE IF NOT EXISTS blockchain_blocks (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_index     INT NOT NULL,
    certificate_id  VARCHAR(50) NOT NULL,
    data_hash       VARCHAR(256) NOT NULL,
    previous_hash   VARCHAR(256) NOT NULL,
    block_hash      VARCHAR(256) NOT NULL,
    timestamp_val   BIGINT NOT NULL,
    nonce           INT DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Verification audit log
CREATE TABLE IF NOT EXISTS verification_logs (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    certificate_id  VARCHAR(50),
    verified_by_ip  VARCHAR(50),
    result          ENUM('VALID', 'INVALID', 'NOT_FOUND') NOT NULL,
    verified_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_certificate_id ON certificates(certificate_id);
CREATE INDEX idx_student_name   ON certificates(student_name);

-- Default admin: username=admin, password=admin123
INSERT INTO admins (username, password, full_name) VALUES
('admin', '$2a$10$slYQmyNdgTY7d00zSCn.OOexRNmlkdHxw06mP/yfU4M4Gq7L6Uzua', 'System Administrator');
