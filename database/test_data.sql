-- 测试数据初始化脚本
-- 密码都是 Test1234
-- BCrypt hash generated with strength 10

-- 清理旧数据
DELETE FROM notifications WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM privacy_settings WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM favorites WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM investment_analyses WHERE investor_user_id IN (1, 2, 3, 4, 5);
DELETE FROM qa_records WHERE investor_user_id IN (1, 2, 3, 4, 5) OR entrepreneur_user_id IN (1, 2, 3, 4, 5);
DELETE FROM applications WHERE investor_user_id IN (1, 2, 3, 4, 5) OR entrepreneur_user_id IN (1, 2, 3, 4, 5);
DELETE FROM investor_questions WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM teasers WHERE project_id IN (SELECT id FROM projects WHERE entrepreneur_user_id IN (1, 2, 3, 4, 5));
DELETE FROM business_plans WHERE project_id IN (SELECT id FROM projects WHERE entrepreneur_user_id IN (1, 2, 3, 4, 5));
DELETE FROM projects WHERE entrepreneur_user_id IN (1, 2, 3, 4, 5);
DELETE FROM investor_profiles WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM entrepreneur_profiles WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM user_verifications WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM user_roles WHERE user_id IN (1, 2, 3, 4, 5);
DELETE FROM roles WHERE id IN (1, 2, 3);
DELETE FROM permissions WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
DELETE FROM users WHERE id IN (1, 2, 3, 4, 5);

-- 插入测试用户 (密码: Test1234)
-- BCrypt hash: $2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW
INSERT INTO users (id, phone, password_hash, email, user_type, real_name, status, is_verified, profile_completion_rate) VALUES
(1, '13800138001', '$2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW', 'investor1@test.com', 'INVESTOR', '张投资', 'ACTIVE', TRUE, 80),
(2, '13800138002', '$2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW', 'investor2@test.com', 'INVESTOR', '李投资', 'ACTIVE', TRUE, 75),
(3, '13800138003', '$2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW', 'entrepreneur1@test.com', 'ENTREPRENEUR', '王创业', 'ACTIVE', TRUE, 90),
(4, '13800138004', '$2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW', 'entrepreneur2@test.com', 'ENTREPRENEUR', '赵创业', 'ACTIVE', FALSE, 60),
(5, '13800138005', '$2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW', 'admin@test.com', 'INVESTOR', '系统管理员', 'ACTIVE', TRUE, 100);

-- 插入投资人资料
INSERT INTO investor_profiles (user_id, institution_name, position, investment_stage, investment_industries, investment_region, investment_range_min, investment_range_max, is_verified, verification_level) VALUES
(1, '红杉资本', '投资总监', '天使轮,A轮,B轮', '科技,医疗,消费', '北京,上海,深圳', 500, 5000, TRUE, 'advanced'),
(2, 'IDG资本', '投资经理', 'A轮,B轮,C轮', '科技,金融', '北京,上海', 1000, 10000, TRUE, 'basic');

-- 插入融资用户资料
INSERT INTO entrepreneur_profiles (user_id, company_name, industry, location, team_size, profile_completion_rate) VALUES
(3, '智能科技有限公司', '人工智能', '北京', 50, 90),
(4, '新能源科技公司', '新能源', '上海', 30, 60);

-- 插入项目
INSERT INTO projects (id, entrepreneur_user_id, project_name, project_code, industry, location, financing_stage, financing_amount, one_line_description, business_description, status) VALUES
(1, 3, 'AI智能客服系统', 'PRJ001', '人工智能', '北京', 'A轮', 2000, '基于大模型的智能客服解决方案', '我们提供基于大语言模型的智能客服系统，帮助企业降低客服成本50%以上', 'ACTIVE'),
(2, 3, '智能数据分析平台', 'PRJ002', '大数据', '北京', '天使轮', 500, '一站式数据分析平台', '为企业提供自助式数据分析工具', 'ACTIVE'),
(3, 4, '分布式光伏发电项目', 'PRJ003', '新能源', '上海', 'A轮', 3000, '工商业分布式光伏解决方案', '专注于工商业屋顶分布式光伏发电系统的设计、安装和运维', 'ACTIVE');

-- 插入Teaser
INSERT INTO teasers (id, project_id, title, subtitle, company_name, ai_summary, key_metrics, company_overview, core_business, status, view_count, favorite_count) VALUES
(1, 1, 'AI智能客服 - 重新定义客户服务', '基于大模型的智能客服解决方案', '智能科技有限公司',
 '{"summary":"领先的AI客服解决方案提供商","highlights":["降低客服成本50%","7x24小时服务","支持多语言"]}',
 '{"teamSize":"50人","revenue":"1000万/年","growthRate":"200%"}',
 '智能科技有限公司成立于2020年，专注于人工智能客服领域',
 '提供基于大语言模型的智能客服系统，支持多渠道接入，智能问答，情感分析等功能',
 'PUBLISHED', 156, 23),
(2, 2, '智能数据分析平台', '让数据分析变得简单', '智能科技有限公司',
 '{"summary":"一站式数据分析平台","highlights":["零代码操作","实时分析","可视化报表"]}',
 '{"teamSize":"30人","revenue":"500万/年","growthRate":"150%"}',
 '专注于企业数据分析领域',
 '提供自助式数据分析工具，支持多种数据源接入',
 'PUBLISHED', 89, 15),
(3, 3, '分布式光伏发电', '清洁能源解决方案', '新能源科技公司',
 '{"summary":"工商业分布式光伏解决方案","highlights":["投资回报期短","绿色环保","政策支持"]}',
 '{"teamSize":"30人","installedCapacity":"50MW","growthRate":"100%"}',
 '专注于工商业分布式光伏发电',
 '提供从设计、安装到运维的一站式光伏服务',
 'PUBLISHED', 67, 8);

-- 插入收藏记录
INSERT INTO favorites (user_id, teaser_id, created_at) VALUES
(1, 1, NOW()),
(1, 2, NOW()),
(2, 1, NOW());

-- 插入问答记录
INSERT INTO qa_records (id, project_id, investor_user_id, entrepreneur_user_id, question, answer, question_status, is_public) VALUES
(1, 1, 1, 3, '贵公司的核心技术壁垒是什么？', '我们拥有自研的大模型微调技术和行业知识图谱，这是我们的核心壁垒', 'ANSWERED', TRUE),
(2, 1, 1, 3, '目前的客户情况如何？', '目前服务超过100家企业客户，包括多家世界500强企业', 'ANSWERED', TRUE),
(3, 2, 2, 3, '产品的技术架构是怎样的？', NULL, 'PENDING', FALSE);

-- 插入申请记录
INSERT INTO applications (id, application_type, investor_user_id, project_id, entrepreneur_user_id, teaser_id, application_status, application_reason) VALUES
(1, 'BP_ACCESS', 1, 1, 3, 1, 'APPROVED', '对AI客服领域非常感兴趣，希望了解更详细的商业计划'),
(2, 'CONTACT_ACCESS', 1, 1, 3, 1, 'PENDING', '希望进一步了解合作机会'),
(3, 'BP_ACCESS', 2, 1, 3, 1, 'PENDING', '正在进行行业研究，需要更多项目信息');

-- 插入隐私设置
INSERT INTO privacy_settings (user_id, allow_show_company_name, allow_show_contact_info, allow_public_qa) VALUES
(3, TRUE, FALSE, TRUE),
(4, TRUE, TRUE, TRUE);

-- 插入通知
INSERT INTO notifications (user_id, notification_type, title, content, is_read) VALUES
(1, 'SYSTEM', '欢迎注册', '欢迎使用投融资对接平台', FALSE),
(3, 'QA', '您有新的问题', '投资人张投资向您发送了一个新问题', FALSE);

-- 插入投资分析
INSERT INTO investment_analyses (id, teaser_id, investor_user_id, analysis_type, analysis_content, score, recommendation) VALUES
(1, 1, 1, 'COMPREHENSIVE', '{"market":"市场空间大","team":"团队经验丰富","technology":"技术领先","risk":"竞争加剧"}', 85, '建议进一步接触'),
(2, 2, 1, 'QUICK', '{"market":"市场一般","team":"团队一般"}', 70, '保持关注');

-- 插入角色
INSERT INTO roles (id, role_code, role_name, role_level, is_enabled) VALUES
(1, 'ADMIN', '管理员', 100, TRUE),
(2, 'INVESTOR', '投资人', 50, TRUE),
(3, 'ENTREPRENEUR', '融资用户', 50, TRUE);

-- 插入权限
INSERT INTO permissions (id, permission_code, permission_name, permission_type, module) VALUES
(1, 'USER_VIEW', '查看用户', 'view', 'user'),
(2, 'USER_EDIT', '编辑用户', 'edit', 'user'),
(3, 'PROJECT_VIEW', '查看项目', 'view', 'project'),
(4, 'PROJECT_EDIT', '编辑项目', 'edit', 'project'),
(5, 'TEASER_VIEW', '查看Teaser', 'view', 'teaser'),
(6, 'TEASER_EDIT', '编辑Teaser', 'edit', 'teaser'),
(7, 'APPLICATION_APPROVE', '审批申请', 'approve', 'application'),
(8, 'VERIFICATION_APPROVE', '审批认证', 'approve', 'verification'),
(9, 'STATISTICS_VIEW', '查看统计', 'view', 'statistics'),
(10, 'SYSTEM_CONFIG', '系统配置', 'config', 'system');

-- 插入用户角色关联
INSERT INTO user_roles (user_id, role_id) VALUES
(5, 1),
(1, 2),
(2, 2),
(3, 3),
(4, 3);

-- 插入问题库模板
INSERT INTO investor_questions (user_id, question, category, is_template) VALUES
(NULL, '贵公司的核心技术壁垒是什么？', '技术', TRUE),
(NULL, '目前的客户情况如何？', '市场', TRUE),
(NULL, '团队背景和经验如何？', '团队', TRUE),
(NULL, '融资资金的主要用途是什么？', '财务', TRUE),
(NULL, '未来3年的发展规划是什么？', '战略', TRUE);
