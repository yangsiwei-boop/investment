package com.investment.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据库同步工具
 * 启动时自动执行同步脚本
 *
 * 设置 sync.database.enabled=true 来启用
 */
@Slf4j
@Component
public class DatabaseSyncRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Value("${sync.database.enabled:false}")
    private boolean syncEnabled;

    public DatabaseSyncRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!syncEnabled) {
            log.info("数据库同步已禁用，设置 sync.database.enabled=true 启用");
            return;
        }

        log.info("==========================================");
        log.info("开始执行数据库表结构同步...");
        log.info("==========================================");

        try {
            // 1. 创建/同步 favorites 表
            syncFavoritesTable();

            // 2. 创建/同步 view_histories 表
            syncViewHistoriesTable();

            // 3. 同步 investment_analyses 表
            syncInvestmentAnalysesTable();

            // 4. 同步 qa_records 表
            syncQaRecordsTable();

            // 5. 同步 notifications 表
            syncNotificationsTable();

            log.info("==========================================");
            log.info("✅ 数据库表结构同步完成!");
            log.info("==========================================");

        } catch (Exception e) {
            log.error("数据库同步失败: {}", e.getMessage(), e);
        }
    }

    private void syncFavoritesTable() {
        log.info("同步 favorites 表...");
        try {
            // 先检查表是否存在
            if (!tableExists("favorites")) {
                log.info("  - 创建 favorites 表...");
                jdbcTemplate.execute("""
                    CREATE TABLE favorites (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL COMMENT '用户ID',
                        teaser_id BIGINT NOT NULL COMMENT 'Teaser ID',
                        group_name VARCHAR(100) COMMENT '分组名称',
                        note TEXT COMMENT '收藏备注',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        UNIQUE KEY uk_user_teaser (user_id, teaser_id),
                        INDEX idx_user_id (user_id),
                        INDEX idx_teaser_id (teaser_id)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表'
                    """);
                log.info("  ✓ favorites 表创建完成");
            } else {
                // 表存在，检查并添加缺失的列
                if (!columnExists("favorites", "group_name")) {
                    jdbcTemplate.execute("ALTER TABLE favorites ADD COLUMN group_name VARCHAR(100) COMMENT '分组名称'");
                    log.info("  - 添加 group_name 列");
                }
                if (!columnExists("favorites", "note")) {
                    jdbcTemplate.execute("ALTER TABLE favorites ADD COLUMN note TEXT COMMENT '收藏备注'");
                    log.info("  - 添加 note 列");
                }
                if (!columnExists("favorites", "updated_at")) {
                    jdbcTemplate.execute("ALTER TABLE favorites ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'");
                    log.info("  - 添加 updated_at 列");
                }
                log.info("  ✓ favorites 表同步完成");
            }
        } catch (Exception e) {
            log.warn("  ✗ favorites 表同步失败: {}", e.getMessage());
        }
    }

    private void syncViewHistoriesTable() {
        log.info("同步 view_histories 表...");
        try {
            // 先检查表是否存在
            if (!tableExists("view_histories")) {
                log.info("  - 创建 view_histories 表...");
                jdbcTemplate.execute("""
                    CREATE TABLE view_histories (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL COMMENT '用户ID',
                        teaser_id BIGINT COMMENT 'Teaser ID',
                        project_id BIGINT COMMENT '项目ID',
                        view_duration INT COMMENT '浏览时长(秒)',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        INDEX idx_user_id (user_id),
                        INDEX idx_teaser_id (teaser_id),
                        INDEX idx_project_id (project_id),
                        INDEX idx_created_at (created_at)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览记录表'
                    """);
                log.info("  ✓ view_histories 表创建完成");
            } else {
                // 表存在，检查并添加缺失的列
                if (!columnExists("view_histories", "teaser_id")) {
                    jdbcTemplate.execute("ALTER TABLE view_histories ADD COLUMN teaser_id BIGINT COMMENT 'Teaser ID'");
                    log.info("  - 添加 teaser_id 列");
                }
                if (!columnExists("view_histories", "project_id")) {
                    jdbcTemplate.execute("ALTER TABLE view_histories ADD COLUMN project_id BIGINT COMMENT '项目ID'");
                    log.info("  - 添加 project_id 列");
                }
                if (!columnExists("view_histories", "view_duration")) {
                    jdbcTemplate.execute("ALTER TABLE view_histories ADD COLUMN view_duration INT COMMENT '浏览时长(秒)'");
                    log.info("  - 添加 view_duration 列");
                }
                if (!columnExists("view_histories", "created_at")) {
                    jdbcTemplate.execute("ALTER TABLE view_histories ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'");
                    log.info("  - 添加 created_at 列");
                }
                log.info("  ✓ view_histories 表同步完成");
            }
        } catch (Exception e) {
            log.warn("  ✗ view_histories 表同步失败: {}", e.getMessage());
        }
    }

    private void syncInvestmentAnalysesTable() {
        log.info("同步 investment_analyses 表...");
        try {
            if (!columnExists("investment_analyses", "analysis_content")) {
                jdbcTemplate.execute("ALTER TABLE investment_analyses ADD COLUMN analysis_content JSON COMMENT '分析内容（JSON格式）'");
                log.info("  - 添加 analysis_content 列");
            }
            if (!columnExists("investment_analyses", "analysis_type")) {
                jdbcTemplate.execute("ALTER TABLE investment_analyses ADD COLUMN analysis_type VARCHAR(50) COMMENT '分析类型'");
                log.info("  - 添加 analysis_type 列");
            }
            if (!columnExists("investment_analyses", "updated_at")) {
                jdbcTemplate.execute("ALTER TABLE investment_analyses ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'");
                log.info("  - 添加 updated_at 列");
            }
            if (!columnExists("investment_analyses", "deleted_at")) {
                jdbcTemplate.execute("ALTER TABLE investment_analyses ADD COLUMN deleted_at DATETIME COMMENT '软删除时间'");
                log.info("  - 添加 deleted_at 列");
            }
            log.info("  ✓ investment_analyses 表同步完成");
        } catch (Exception e) {
            log.warn("  ✗ investment_analyses 表同步失败: {}", e.getMessage());
        }
    }

    private void syncQaRecordsTable() {
        log.info("同步 qa_records 表...");
        try {
            if (!columnExists("qa_records", "investor_message")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN investor_message TEXT COMMENT '投资人留言'");
                log.info("  - 添加 investor_message 列");
            }
            if (!columnExists("qa_records", "draft_answer")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN draft_answer TEXT COMMENT '草稿回答内容'");
                log.info("  - 添加 draft_answer 列");
            }
            if (!columnExists("qa_records", "is_from_question_library")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN is_from_question_library BOOLEAN DEFAULT FALSE COMMENT '是否来自问题库'");
                log.info("  - 添加 is_from_question_library 列");
            }
            if (!columnExists("qa_records", "question_library_id")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN question_library_id BIGINT COMMENT '问题库问题ID'");
                log.info("  - 添加 question_library_id 列");
            }
            if (!columnExists("qa_records", "allow_public")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN allow_public BOOLEAN DEFAULT TRUE COMMENT '融资方是否允许公开此问答'");
                log.info("  - 添加 allow_public 列");
            }
            if (!columnExists("qa_records", "use_privacy_setting")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN use_privacy_setting BOOLEAN DEFAULT TRUE COMMENT '是否使用隐私设置'");
                log.info("  - 添加 use_privacy_setting 列");
            }
            if (!columnExists("qa_records", "investor_viewed_at")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN investor_viewed_at DATETIME COMMENT '投资人查看回复的时间'");
                log.info("  - 添加 investor_viewed_at 列");
            }
            if (!columnExists("qa_records", "created_at")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'");
                log.info("  - 添加 created_at 列");
            }
            if (!columnExists("qa_records", "updated_at")) {
                jdbcTemplate.execute("ALTER TABLE qa_records ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'");
                log.info("  - 添加 updated_at 列");
            }
            log.info("  ✓ qa_records 表同步完成");
        } catch (Exception e) {
            log.warn("  ✗ qa_records 表同步失败: {}", e.getMessage());
        }
    }

    private void syncNotificationsTable() {
        log.info("同步 notifications 表...");
        try {
            if (!columnExists("notifications", "related_id")) {
                jdbcTemplate.execute("ALTER TABLE notifications ADD COLUMN related_id BIGINT COMMENT '关联ID'");
                log.info("  - 添加 related_id 列");
            }
            if (!columnExists("notifications", "related_type")) {
                jdbcTemplate.execute("ALTER TABLE notifications ADD COLUMN related_type VARCHAR(50) COMMENT '关联类型'");
                log.info("  - 添加 related_type 列");
            }
            if (!columnExists("notifications", "is_read")) {
                jdbcTemplate.execute("ALTER TABLE notifications ADD COLUMN is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读'");
                log.info("  - 添加 is_read 列");
            }
            if (!columnExists("notifications", "created_at")) {
                jdbcTemplate.execute("ALTER TABLE notifications ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'");
                log.info("  - 添加 created_at 列");
            }
            if (!columnExists("notifications", "updated_at")) {
                jdbcTemplate.execute("ALTER TABLE notifications ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'");
                log.info("  - 添加 updated_at 列");
            }
            if (!columnExists("notifications", "deleted_at")) {
                jdbcTemplate.execute("ALTER TABLE notifications ADD COLUMN deleted_at DATETIME COMMENT '删除时间'");
                log.info("  - 添加 deleted_at 列");
            }
            log.info("  ✓ notifications 表同步完成");
        } catch (Exception e) {
            log.warn("  ✗ notifications 表同步失败: {}", e.getMessage());
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean tableExists(String tableName) {
        try {
            String sql = """
                SELECT COUNT(*) FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_NAME = ?
                """;
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("检查表存在失败: {}", tableName);
            return false;
        }
    }

    /**
     * 检查列是否存在
     */
    private boolean columnExists(String tableName, String columnName) {
        try {
            String sql = """
                SELECT COUNT(*) FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_NAME = ?
                AND COLUMN_NAME = ?
                """;
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("检查列存在失败: {}.{}", tableName, columnName);
            return false;
        }
    }
}
