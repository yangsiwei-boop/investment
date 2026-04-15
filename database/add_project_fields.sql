-- ============================================
-- 为 projects 表添加缺失字段
-- 执行时间: 2026-04-15
-- 兼容 MySQL 8
-- ============================================

USE investment;

-- 添加 target_market 字段
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'investment' AND TABLE_NAME = 'projects' AND COLUMN_NAME = 'target_market');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE projects ADD COLUMN target_market TEXT COMMENT ''目标市场'' AFTER business_model',
    'SELECT ''target_market already exists'' AS result');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 icon_emoji 字段
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'investment' AND TABLE_NAME = 'projects' AND COLUMN_NAME = 'icon_emoji');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE projects ADD COLUMN icon_emoji VARCHAR(10) COMMENT ''图标emoji'' AFTER is_anonymous',
    'SELECT ''icon_emoji already exists'' AS result');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 tags 字段
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'investment' AND TABLE_NAME = 'projects' AND COLUMN_NAME = 'tags');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE projects ADD COLUMN tags VARCHAR(1000) COMMENT ''标签列表(JSON数组)'' AFTER icon_emoji',
    'SELECT ''tags already exists'' AS result');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT '✅ projects 表字段添加完成!' AS result;
