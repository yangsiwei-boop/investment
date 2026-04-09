-- 创建问答回复表
CREATE TABLE IF NOT EXISTS qa_replies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    qa_record_id BIGINT NOT NULL COMMENT '关联的问答记录ID',
    user_id BIGINT NOT NULL COMMENT '回复者用户ID',
    content TEXT NOT NULL COMMENT '回复内容',
    is_public BOOLEAN DEFAULT TRUE COMMENT '是否公开',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (qa_record_id) REFERENCES qa_records(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_qa_record_id (qa_record_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问答回复表';

-- 迁移历史数据：将 qa_records 中已有的 answer 迁移到 qa_replies
INSERT INTO qa_replies (qa_record_id, user_id, content, is_public, created_at)
SELECT id, entrepreneur_user_id, answer, is_public, COALESCE(answered_at, updated_at)
FROM qa_records
WHERE answer IS NOT NULL AND answer != '' AND entrepreneur_user_id IS NOT NULL;
