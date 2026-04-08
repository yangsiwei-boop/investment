-- ============================================
-- 数据库表结构同步脚本
-- 执行时间: 2026-04-08
-- 目的: 修复实体类与数据库表结构不一致的问题
-- ============================================

USE investment;

-- ============================================
-- 1. 修复 favorites 表 (收藏功能)
-- ============================================
-- 添加缺失的列
ALTER TABLE favorites ADD COLUMN IF NOT EXISTS group_name VARCHAR(100) COMMENT '分组名称';
ALTER TABLE favorites ADD COLUMN IF NOT EXISTS note TEXT COMMENT '收藏备注';
ALTER TABLE favorites ADD COLUMN IF NOT EXISTS updated_at DATETIME COMMENT '更新时间';

-- 验证结构
SELECT 'favorites 表结构:' AS info;
DESCRIBE favorites;

-- ============================================
-- 2. 修复 investment_analyses 表 (AI分析功能)
-- ============================================
-- 添加缺失的列
ALTER TABLE investment_analyses ADD COLUMN IF NOT EXISTS analysis_type VARCHAR(50) COMMENT '分析类型';
ALTER TABLE investment_analyses ADD COLUMN IF NOT EXISTS analysis_content JSON COMMENT '分析内容(JSON格式)';

-- 验证结构
SELECT 'investment_analyses 表结构:' AS info;
DESCRIBE investment_analyses;

-- ============================================
-- 3. 修复 view_histories 表 (浏览记录)
-- ============================================
-- 确保所有列都存在
ALTER TABLE view_histories ADD COLUMN IF NOT EXISTS id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;
ALTER TABLE view_histories ADD COLUMN IF NOT EXISTS user_id BIGINT NOT NULL COMMENT '用户ID';
ALTER TABLE view_histories ADD COLUMN IF NOT EXISTS teaser_id BIGINT COMMENT 'Teaser ID';
ALTER TABLE view_histories ADD COLUMN IF NOT EXISTS project_id BIGINT COMMENT '项目ID';
ALTER TABLE view_histories ADD COLUMN IF NOT EXISTS view_duration INT COMMENT '浏览时长(秒)';
ALTER TABLE view_histories ADD COLUMN IF NOT EXISTS created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

-- 添加索引（如果不存在）
CREATE INDEX IF NOT EXISTS idx_view_histories_user_id ON view_histories(user_id);
CREATE INDEX IF NOT EXISTS idx_view_histories_teaser_id ON view_histories(teaser_id);
CREATE INDEX IF NOT EXISTS idx_view_histories_created_at ON view_histories(created_at);

-- 验证结构
SELECT 'view_histories 表结构:' AS info;
DESCRIBE view_histories;

-- ============================================
-- 4. 修复 qa_records 表 (问答记录)
-- ============================================
-- 添加缺失的列
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS investor_message TEXT COMMENT '投资人留言';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS draft_answer TEXT COMMENT '草稿回答内容';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS is_from_question_library BOOLEAN DEFAULT FALSE COMMENT '是否来自问题库';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS question_library_id BIGINT COMMENT '问题库问题ID';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS allow_public BOOLEAN DEFAULT TRUE COMMENT '融资方是否允许公开此问答';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS use_privacy_setting BOOLEAN DEFAULT TRUE COMMENT '是否使用隐私设置';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS investor_viewed_at DATETIME COMMENT '投资人查看回复的时间';
ALTER TABLE qa_records ADD COLUMN IF NOT EXISTS entrepreneur_viewed_at DATETIME COMMENT '融资方查看问题的时间';

-- 验证结构
SELECT 'qa_records 表结构:' AS info;
DESCRIBE qa_records;

-- ============================================
-- 5. 修复 notifications 表 (通知功能)
-- ============================================
-- 确保所有列都存在
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS user_id BIGINT NOT NULL COMMENT '接收用户ID';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS notification_type VARCHAR(50) NOT NULL COMMENT '通知类型';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS title VARCHAR(255) NOT NULL COMMENT '通知标题';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS content TEXT COMMENT '通知内容';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS related_id BIGINT COMMENT '关联ID';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS related_type VARCHAR(50) COMMENT '关联类型';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读';
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS created_at DATETIME DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS deleted_at DATETIME;

-- 添加索引
CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_is_read ON notifications(is_read);

-- 验证结构
SELECT 'notifications 表结构:' AS info;
DESCRIBE notifications;

-- ============================================
-- 6. 修复 entrepreneur_profiles 表
-- ============================================
-- 添加缺失的列
ALTER TABLE entrepreneur_profiles ADD COLUMN IF NOT EXISTS profile_completion_rate INT DEFAULT 0 COMMENT '资料完善度';

-- 验证结构
SELECT 'entrepreneur_profiles 表结构:' AS info;
DESCRIBE entrepreneur_profiles;

-- ============================================
-- 7. 检查 teasers 表
-- ============================================
SELECT 'teasers 表结构:' AS info;
DESCRIBE teasers;

-- ============================================
-- 8. 检查 projects 表
-- ============================================
SELECT 'projects 表结构:' AS info;
DESCRIBE projects;

-- ============================================
-- 完成提示
-- ============================================
SELECT '✅ 数据库表结构同步完成!' AS result;
