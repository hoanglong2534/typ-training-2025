-- Update admin password to BCrypt hash of 'admin123'
-- Hash generated using BCrypt with strength 10
UPDATE users 
SET password_hash = '$2a$10$ZWC04/ZJdPKYDUj0nfqJa.UQ58VGzEsoFX1NIhVPmzjBReSxwfXGO'
WHERE username = 'admin';
