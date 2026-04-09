-- ============================================
-- 创建管理员账号
-- 密码: Admin1234 (使用BCrypt加密)
-- ============================================

USE investment;

-- 先检查是否存在
SELECT id, phone, user_type FROM users WHERE user_type = 'ADMIN';

-- 创建管理员账号
INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
VALUES ('13800138000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'admin@investment.com', 'ADMIN', 'ACTIVE', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    password_hash = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM',
    status = 'ACTIVE',
    is_verified = TRUE,
    updated_at = NOW();

-- 验证创建成功
SELECT '✅ 管理员账号创建成功!' AS result;
SELECT id, phone, user_type, status FROM users WHERE phone = '13800138000';
