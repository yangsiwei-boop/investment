-- ============================================
-- 测试数据初始化脚本
-- 执行时间: 2026-04-08
-- ============================================

USE investment;

-- ============================================
-- 1. 确保测试用户存在
-- ============================================

-- 投资人测试账号
INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
VALUES ('13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'investor@test.com', 'INVESTOR', 'ACTIVE', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 融资方测试账号
INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
VALUES ('13800138003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'entrepreneur@test.com', 'ENTREPRENEUR', 'ACTIVE', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 管理员测试账号 (密码: Admin1234)
INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
VALUES ('13800138000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'admin@test.com', 'ADMIN', 'ACTIVE', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- 2. 确保用户资料存在
-- ============================================

-- 投资人资料
INSERT INTO investor_profiles (user_id, institution_name, created_at, updated_at)
SELECT id, '测试投资机构', NOW(), NOW() FROM users WHERE phone = '13800138001'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 融资方资料
INSERT INTO entrepreneur_profiles (user_id, company_name, created_at, updated_at)
SELECT id, '测试融资公司', NOW(), NOW() FROM users WHERE phone = '13800138003'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- 3. 创建测试项目
-- ============================================

INSERT INTO projects (entrepreneur_user_id, name, industry, financing_stage, summary, status, created_at, updated_at)
SELECT id, 'AI智能投资平台', 'ARTIFICIAL_INTELLIGENCE', 'SERIES_A', '基于人工智能的投资决策辅助平台', 'ACTIVE', NOW(), NOW()
FROM users WHERE phone = '13800138003'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- 4. 创建测试Teaser
-- ============================================

INSERT INTO teasers (project_id, title, summary, status, view_count, created_at, updated_at)
SELECT p.id, 'AI投资平台Teaser', '领先的AI驱动投资决策平台', 'PUBLISHED', 100, NOW(), NOW()
FROM projects p
JOIN users u ON p.entrepreneur_user_id = u.id
WHERE u.phone = '13800138003'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- 5. 验证数据
-- ============================================

SELECT '--- 用户数据 ---' AS info;
SELECT id, phone, user_type, status FROM users WHERE phone IN ('13800138000', '13800138001', '13800138003');

SELECT '--- 项目数据 ---' AS info;
SELECT id, name, status FROM projects LIMIT 5;

SELECT '--- Teaser数据 ---' AS info;
SELECT id, title, status FROM teasers LIMIT 5;

SELECT '✅ 测试数据初始化完成!' AS result;
