-- 为investor_activities表添加缺失的列以支持完整功能
-- 执行时间: 2024-04-08

-- 添加teaser_id列（必须！关联Teaser）
ALTER TABLE investor_activities
ADD COLUMN IF NOT EXISTS teaser_id BIGINT COMMENT 'Teaser ID';

-- 添加activity_type列（必须！活动类型）
ALTER TABLE investor_activities
ADD COLUMN IF NOT EXISTS activity_type VARCHAR(20) NOT NULL DEFAULT 'VIEW' COMMENT '活动类型(VIEW, FAVORITE, ANALYZE, APPLY等)';

-- 添加group_name列（用于收藏分组）
ALTER TABLE investor_activities
ADD COLUMN IF NOT EXISTS group_name VARCHAR(100) COMMENT '分组名称（用于收藏分组等）';

-- 添加notes列（用于活动备注）
ALTER TABLE investor_activities
ADD COLUMN IF NOT EXISTS notes TEXT COMMENT '活动备注';

-- 添加updated_at列（用于更新时间）
ALTER TABLE investor_activities
ADD COLUMN IF NOT EXISTS updated_at DATETIME COMMENT '更新时间';

-- 添加索引
CREATE INDEX IF NOT EXISTS idx_investor_activities_teaser_id ON investor_activities(teaser_id);
CREATE INDEX IF NOT EXISTS idx_investor_activities_activity_type ON investor_activities(activity_type);

-- 验证列已添加
DESCRIBE investor_activities;
