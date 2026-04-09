#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""插入测试数据"""

import pymysql
import sys
import random
from datetime import datetime, timedelta, date

sys.stdout.reconfigure(encoding='utf-8')

conn = pymysql.connect(
    host='36.150.231.203', port=3306,
    user='root', password='root_PASS866.',
    database='investment', charset='utf8mb4', autocommit=False
)
cur = conn.cursor()
now = datetime.now()

# 密码统一用 Test1234 的BCrypt hash
PWD_HASH = '$2a$10$EqKcp1WFKVMKEbLBCPTxOeOZAjLwRTjnCsmFnBJeKBDBoUdQ/5Phe'

def random_date(days_ago_min, days_ago_max):
    return now - timedelta(days=random.randint(days_ago_min, days_ago_max))

# ====================== 1. 投资人用户 + 资料表 ======================
print('=== 1. Creating investor users + profiles ===')

investors = [
    ('13800138010', '王建国', 'wangjg@capital.com', '红杉资本', '合伙人',
     'ai,fintech,healthcare', 'a,b,c', 'beijing,shanghai', 500, 10000, '专注AI和金融科技早期投资，20年投资经验'),
    ('13800138011', '李明远', 'limy@venture.cn', 'IDG资本', '投资总监',
     'enterprise_service,ai,new_energy', 'b,c,d', 'shanghai,hangzhou', 1000, 50000, '深耕企业服务和新能源赛道'),
    ('13800138012', '张晓芳', 'zhangxf@fund.com', '高瓴资本', '执行董事',
     'healthcare,consumer,education', 'a,b,pre_ipo', 'shenzhen,beijing', 2000, 100000, '医疗健康和消费领域资深投资人'),
    ('13800138013', '陈志强', 'chenzq@pe.com', '经纬中国', '副总裁',
     'ai,manufacturing,fintech', 'seed,angel,pre_a', 'hangzhou,beijing', 100, 5000, '关注智能制造和AI应用'),
    ('13800138014', '刘雅婷', 'liuyt@angel.cn', '真格基金', '投资经理',
     'education,entertainment,consumer', 'seed,angel', 'beijing', 50, 2000, '关注教育科技和新消费品牌'),
    ('13800138015', '赵鹏飞', 'zhaopf@vc.com', '北极光创投', '董事总经理',
     'ai,new_energy,fintech', 'a,b,c', 'shanghai', 500, 20000, '新能源和AI双赛道布局'),
]

investor_uids = []
for phone, real_name, email, inst, position, industries, stages, regions, min_amt, max_amt, philosophy in investors:
    # Check if already exists
    cur.execute('SELECT id FROM users WHERE phone = %s', (phone,))
    existing = cur.fetchone()
    if existing:
        uid = existing[0]
        investor_uids.append(uid)
        print(f'  Investor (skip): {real_name} ({inst}), uid={uid}')
        continue

    cur.execute('''INSERT INTO users (phone, email, password_hash, user_type, status, real_name, is_verified, created_at, updated_at, last_login_at)
        VALUES (%s, %s, %s, 'INVESTOR', 'ACTIVE', %s, 1, %s, %s, %s)''',
        (phone, email, PWD_HASH, real_name, random_date(30, 90), now, random_date(0, 5)))
    uid = cur.lastrowid
    investor_uids.append(uid)

    cur.execute('''INSERT INTO investor_profiles
        (user_id, institution_name, position, investment_industries, investment_stage,
        investment_region, investment_range_min, investment_range_max, investment_philosophy,
        is_verified, created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, 1, %s, %s)''',
        (uid, inst, position, industries, stages, regions, min_amt * 10000, max_amt * 10000,
         philosophy, random_date(30, 90), now))
    print(f'  Investor: {real_name} ({inst}), uid={uid}')

# ====================== 2. 融资方用户 + 资料表 ======================
print('\n=== 2. Creating entrepreneur users + profiles ===')

entrepreneurs = [
    ('13800138020', '周明辉', 'zhm@smartai.com', '智谱AI科技', 'ai', 'a', '北京', 50, 5000, 'contact@smartai.com', '领先的AI大模型研发企业，拥有自主知识产权'),
    ('13800138021', '吴晓燕', 'wxy@healthtech.cn', '康乐健康科技', 'healthcare', 'b', '上海', 100, 8000, 'info@healthtech.cn', '数字医疗解决方案提供商，服务300+医院'),
    ('13800138022', '孙伟', 'sunw@finedge.com', '数联金融科技', 'fintech', 'a', '深圳', 60, 3000, 'contact@finedge.com', '智能风控和量化交易平台，管理资产超百亿'),
    ('13800138023', '郑丽华', 'zhenglh@edutech.com', '启明教育科技', 'education', 'angel', '杭州', 30, 1000, 'info@edutech.com', 'AI自适应学习平台，覆盖K12全科'),
    ('13800138024', '黄志远', 'huangzy@newenergy.cn', '绿能新能源', 'new_energy', 'c', '合肥', 200, 20000, 'contact@newenergy.cn', '固态电池技术研发与产业化，已获多项专利'),
    ('13800138025', '马超', 'mac@gamefun.com', '趣玩游戏科技', 'entertainment', 'pre_a', '成都', 50, 2000, 'info@gamefun.com', '元宇宙社交游戏开发，月活用户超500万'),
    ('13800138026', '林小慧', 'linxh@retailai.com', '智慧零售科技', 'consumer', 'a', '广州', 80, 4000, 'contact@retailai.com', 'AI驱动的零售供应链优化，合作200+品牌'),
    ('13800138027', '杨帆', 'yangf@cloudservice.cn', '云端企服科技', 'enterprise_service', 'b', '北京', 150, 6000, 'info@cloudservice.cn', '企业级SaaS云服务平台，ARR超2亿'),
]

ent_uids = []
for phone, real_name, email, company, industry, stage, location, team_size, target_amt, contact_email, intro in entrepreneurs:
    cur.execute('SELECT id FROM users WHERE phone = %s', (phone,))
    existing = cur.fetchone()
    if existing:
        uid = existing[0]
        ent_uids.append(uid)
        print(f'  Entrepreneur (skip): {real_name} ({company}), uid={uid}')
        continue

    cur.execute('''INSERT INTO users (phone, email, password_hash, user_type, status, real_name, is_verified, created_at, updated_at, last_login_at)
        VALUES (%s, %s, %s, 'ENTREPRENEUR', 'ACTIVE', %s, 1, %s, %s, %s)''',
        (phone, email, PWD_HASH, real_name, random_date(20, 120), now, random_date(0, 3)))
    uid = cur.lastrowid
    ent_uids.append(uid)

    cur.execute('''INSERT INTO entrepreneur_profiles
        (user_id, company_name, industry, company_stage, location, team_size,
        introduction, contact_email, created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)''',
        (uid, company, industry, stage, location, team_size,
         intro, contact_email, random_date(20, 120), now))
    print(f'  Entrepreneur: {real_name} ({company}), uid={uid}')

conn.commit()
print(f'\nCreated {len(investor_uids)} investors, {len(ent_uids)} entrepreneurs')

# ====================== 3. 项目 ======================
print('\n=== 3. Creating projects ===')

projects_data = [
    ('智谱AI大模型平台', '基于自研大语言模型的企业级AI解决方案', 'ai', 'a', 5000, '模型训练和商业化推广', '北京', 'published', 'B2B SaaS', '企业级AI应用市场', '核心算法壁垒和数据积累'),
    ('数字医疗诊断系统', 'AI辅助医学影像诊断平台', 'healthcare', 'b', 8000, '产品研发和市场拓展', '上海', 'published', 'B2B2C', '全国三甲医院', '已获NMPA认证，服务300+医院'),
    ('智能风控引擎', '基于AI的实时风控和反欺诈系统', 'fintech', 'a', 3000, '技术升级和团队扩张', '深圳', 'published', 'B2B SaaS', '银行和金融机构', '毫秒级响应，准确率99.5%'),
    ('AI自适应学习平台', '个性化K12在线教育平台', 'education', 'angel', 1000, '内容研发和用户增长', '杭州', 'published', 'B2C', 'K12学生和家长', 'AI个性化推荐，提分效果显著'),
    ('固态电池产业化', '新一代固态电池量产技术', 'new_energy', 'c', 20000, '产线建设和研发投入', '合肥', 'published', 'B2B', '新能源汽车厂商', '能量密度提升50%，成本降低30%'),
    ('元宇宙社交游戏', '沉浸式虚拟社交平台', 'entertainment', 'pre_a', 2000, '产品迭代和市场推广', '成都', 'published', 'B2C', '年轻用户群体', '月活500万，用户留存率行业领先'),
    ('智慧零售供应链', 'AI驱动的零售供应链优化平台', 'consumer', 'a', 4000, '技术升级和客户拓展', '广州', 'published', 'B2B SaaS', '零售品牌商', '合作200+品牌，降低库存成本40%'),
    ('企业云服务平台', '一站式企业级SaaS解决方案', 'enterprise_service', 'b', 6000, '产品线扩展和国际化', '北京', 'published', 'B2B SaaS', '中大型企业', 'ARR超2亿，续约率95%'),
    ('智能制造执行系统', '工业4.0智能工厂解决方案', 'manufacturing', 'b', 5000, '产品研发和海外市场', '苏州', 'published', 'B2B', '制造业企业', '帮助工厂提升效率30%，降低成本25%'),
    ('AI芯片设计平台', '云端AI芯片EDA工具', 'ai', 'pre_a', 3000, '产品研发和IP布局', '上海', 'draft', 'B2B SaaS', '芯片设计公司', '缩短芯片设计周期50%'),
    ('新能源汽车充电网络', '智能充电桩运营平台', 'new_energy', 'a', 8000, '网络铺设和运营', '深圳', 'draft', 'B2B2C', '新能源车主', '已覆盖20个城市，1万个充电桩'),
    ('区块链供应链金融', '基于区块链的供应链金融平台', 'fintech', 'seed', 500, 'MVP开发和种子客户', '杭州', 'draft', 'B2B', '中小企业', '区块链+AI双技术驱动'),
    ('AI内容生成平台', '多模态AI内容创作SaaS', 'ai', 'a', 2000, '模型训练和市场推广', '北京', 'draft', 'B2B SaaS', '内容创作者和企业', '支持文字、图片、视频多模态生成'),
    ('在线心理咨询平台', 'AI辅助的心理健康服务平台', 'healthcare', 'seed', 800, '平台开发和运营', '上海', 'archived', 'B2C', '都市白领', 'AI预筛查+真人咨询结合'),
    ('智能仓储物流系统', 'AGV机器人+AI调度系统', 'manufacturing', 'b', 4000, '产品升级和产能扩张', '广州', 'archived', 'B2B', '电商和物流企业', '仓储效率提升60%'),
]

project_ids = []
for name, summary, industry, stage, amount, purpose, location, status, model, market, advantage in projects_data:
    eid = random.choice(ent_uids)
    cur.execute('''INSERT INTO projects (entrepreneur_user_id, project_name, one_line_description, industry, financing_stage,
        financing_amount, business_description, location, status, business_model, market_size, competitive_advantage,
        created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)''',
        (eid, name, summary, industry, stage, amount, purpose, location, status, model, market, advantage,
         random_date(5, 60), now))
    pid = cur.lastrowid
    project_ids.append(pid)
    print(f'  Project: {name} [{industry}/{stage}/{status}], pid={pid}')

conn.commit()
print(f'Created {len(project_ids)} projects')

# ====================== 4. Teaser (为 published 项目创建) ======================
print('\n=== 4. Creating teasers ===')

teaser_data = [
    ('智谱AI - 企业级AI解决方案领导者', '国内领先的大语言模型研发企业', 'ai', 'a', 5000, '北京',
     ['核心技术自主可控','客户续约率超95%','团队来自清华北大'], 'B2B SaaS', '企业级AI应用市场', '核心算法壁垒和数据积累', '🤖'),
    ('康乐健康 - AI医学影像诊断', '已获NMPA认证的AI辅助诊断平台', 'healthcare', 'b', 8000, '上海',
     ['已获医疗器械认证','服务300+三甲医院','诊断准确率97%'], 'B2B2C', '全国医疗机构', '医疗数据壁垒和合规优势', '🏥'),
    ('数联金融 - 智能风控引擎', '毫秒级实时风控和反欺诈', 'fintech', 'a', 3000, '深圳',
     ['毫秒级响应','准确率99.5%','服务50+金融机构'], 'B2B SaaS', '银行和金融机构', '技术领先和客户积累', '💰'),
    ('启明教育 - AI自适应学习', '个性化K12在线教育平台', 'education', 'angel', 1000, '杭州',
     ['AI个性化推荐','提分效果显著','用户增长率月均30%'], 'B2C', 'K12学生和家长', '教学效果数据验证', '📚'),
    ('绿能新能源 - 固态电池量产', '新一代固态电池技术突破', 'new_energy', 'c', 20000, '合肥',
     ['能量密度提升50%','成本降低30%','已获20项专利'], 'B2B', '新能源汽车厂商', '核心专利和量产能力', '🔋'),
    ('趣玩游戏 - 元宇宙社交', '月活500万的虚拟社交平台', 'entertainment', 'pre_a', 2000, '成都',
     ['月活500万','用户留存率45%','ARPU值持续提升'], 'B2C', 'Z世代年轻用户', '先发优势和用户粘性', '🎮'),
    ('智慧零售 - AI供应链优化', '合作200+品牌的零售科技', 'consumer', 'a', 4000, '广州',
     ['合作200+品牌','降低库存成本40%','覆盖全渠道'], 'B2B SaaS', '零售品牌商', '行业know-how和数据', '🛒'),
    ('云端企服 - 企业级SaaS平台', 'ARR超2亿的企业服务平台', 'enterprise_service', 'b', 6000, '北京',
     ['ARR超2亿','续约率95%','服务1000+企业'], 'B2B SaaS', '中大型企业', '产品矩阵和客户壁垒', '☁️'),
]

teaser_ids = []
# 用前8个published项目
published_pids = project_ids[:8]
for i, (title, summary, industry, stage, amount, location, highlights, model, market, advantage, emoji) in enumerate(teaser_data):
    if i >= len(published_pids):
        break
    pid = published_pids[i]
    cur.execute('''INSERT INTO teasers (project_id, title, subtitle, icon_emoji, company_overview,
        investment_highlights, core_business, market_size, competitive_advantage,
        status, view_count, favorite_count, created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, 'published', %s, %s, %s, %s)''',
        (pid, title, summary, emoji, summary,
         ','.join(highlights), model, market, advantage,
         random.randint(50, 500), random.randint(5, 50),
         random_date(5, 40), now))
    tid = cur.lastrowid
    teaser_ids.append(tid)
    print(f'  Teaser: {title}, tid={tid}')

conn.commit()
print(f'Created {len(teaser_ids)} teasers')

# ====================== 5. 收藏记录 ======================
print('\n=== 5. Creating favorites ===')

# 让投资人收藏各种Teaser
groups = ['重点关注', 'AI赛道', '医疗健康', '金融科技', '新能源', '待跟进']
all_investor_uids = investor_uids + [22]  # include test investor
count = 0
for inv_uid in all_investor_uids:
    for tid in random.sample(teaser_ids, min(random.randint(2, 5), len(teaser_ids))):
        note = random.choice(['非常感兴趣，需要深入调研', '团队背景优秀', '赛道前景好', '待内部讨论', ''])
        cur.execute('''INSERT IGNORE INTO favorites (user_id, teaser_id, group_name, note, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s)''',
            (inv_uid, tid, random.choice(groups), note, random_date(1, 20), now))
        count += 1

conn.commit()
print(f'Created ~{count} favorites')

# ====================== 6. 浏览记录 ======================
print('\n=== 6. Creating view histories ===')

count = 0
for inv_uid in all_investor_uids:
    for tid in random.sample(teaser_ids, min(random.randint(3, 8), len(teaser_ids))):
        duration = random.randint(10, 300)
        cur.execute('''INSERT INTO view_histories (user_id, teaser_id, view_duration, created_at)
            VALUES (%s, %s, %s, %s)''',
            (inv_uid, tid, duration, random_date(1, 15)))
        count += 1

conn.commit()
print(f'Created {count} view histories')

# ====================== 7. 投资分析 ======================
print('\n=== 7. Creating investment analyses ===')

analysis_types = ['comprehensive', 'market', 'team', 'financial', 'competitive']
analysis_statuses = ['completed', 'completed', 'completed', 'processing']
recommendations = ['强烈推荐', '推荐', '建议关注', '谨慎评估']
count = 0
for inv_uid in all_investor_uids[:4]:
    for tid in random.sample(teaser_ids, min(random.randint(1, 3), len(teaser_ids))):
        atype = random.choice(analysis_types)
        score = round(random.uniform(60, 95), 1)
        rec = random.choice(recommendations)
        content = f'{{\"summary\":\"该项目在{atype}方面表现优秀\",\"strengths\":[\"技术壁垒高\",\"市场空间大\",\"团队背景强\"],\"risks\":[\"竞争加剧\",\"政策变化\"],\"score\":{score},\"recommendation\":\"{rec}\"}}'
        cur.execute('''INSERT INTO investment_analyses (investor_user_id, teaser_id, analysis_type, analysis_content,
            score, recommendation, status, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)''',
            (inv_uid, tid, atype, content, score, rec, random.choice(analysis_statuses),
             random_date(1, 10), now))
        count += 1

conn.commit()
print(f'Created {count} investment analyses')

# ====================== 8. 问答记录 ======================
print('\n=== 8. Creating QA records ===')

questions = [
    '贵公司的核心技术壁垒是什么？',
    '目前的主要竞争对手有哪些？如何应对？',
    '团队的背景和组成是怎样的？',
    '商业模式的盈利点在哪里？',
    '未来12个月的战略规划是什么？',
    '目前的客户获取成本和生命周期价值是多少？',
    '技术路线图是怎样的？',
    '市场份额和增长情况如何？',
    '资金使用的具体计划是什么？',
    '退出机制是如何规划的？',
]

answers = [
    '我们的核心技术壁垒在于自研的算法和多年的数据积累，目前已申请20+项发明专利。',
    '目前市场处于早期阶段，主要竞争对手有X公司和Y公司。我们的差异化在于技术领先和服务深度。',
    '核心团队由来自BAT的资深工程师和行业专家组成，平均从业经验超过10年。',
    '主要通过SaaS订阅和增值服务盈利，目前客单价XX万，毛利率达到70%。',
    '未来12个月我们计划完成B轮融资，拓展到5个新城市，团队规模翻倍。',
    '目前CAC约为5000元，LTV约为5万元，LTV/CAC比达到10:1。',
    '我们计划在Q3推出2.0版本，新增AI辅助决策功能，Q4开放API平台。',
    '目前在我们专注的细分市场占有率约15%，季度增长率保持20%以上。',
    '计划将40%用于研发，30%用于市场拓展，20%用于团队建设，10%作为运营储备。',
    '我们计划在3-5年内实现IPO，同时也会考虑战略并购的可能性。',
]

categories = ['business_model', 'technology', 'team', 'market', 'finance', 'strategy']

count = 0
# Investor questions on teasers
for inv_uid in random.sample(all_investor_uids, min(4, len(all_investor_uids))):
    for tid in random.sample(teaser_ids, min(random.randint(2, 4), len(teaser_ids))):
        qi = random.randint(0, len(questions) - 1)
        question = questions[qi]
        answer = answers[qi] if random.random() > 0.3 else None
        status = 'answered' if answer else random.choice(['pending', 'pending', 'ignored'])
        is_public = random.choice([True, True, False])

        cur.execute('''INSERT INTO qa_records (investor_user_id, entrepreneur_user_id, teaser_id,
            question, answer, category, question_status, is_public, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)''',
            (inv_uid, random.choice(ent_uids), tid, question, answer,
             random.choice(categories), status, is_public,
             random_date(1, 15), now))
        count += 1

conn.commit()
print(f'Created {count} QA records')

# ====================== 9. 申请记录 ======================
print('\n=== 9. Creating applications ===')

app_types = ['get_bp', 'contact_company', 'view_contact']
app_statuses = ['pending', 'approved', 'approved', 'rejected', 'pending']
reasons = [
    '对项目非常感兴趣，希望了解更多细节',
    '我们的投资策略与该项目高度匹配',
    '希望与创始团队深入交流',
    '正在进行行业研究，需要了解项目情况',
    '已有投资意向，需要查看完整BP',
]

count = 0
for inv_uid in random.sample(all_investor_uids, min(5, len(all_investor_uids))):
    for tid in random.sample(teaser_ids, min(random.randint(1, 3), len(teaser_ids))):
        atype = random.choice(app_types)
        astatus = random.choice(app_statuses)
        review_comment = None
        if astatus == 'approved':
            review_comment = '审核通过，请查看'
        elif astatus == 'rejected':
            review_comment = '信息不完整，请补充'

        cur.execute('''INSERT INTO applications (investor_user_id, entrepreneur_user_id, teaser_id,
            application_type, reason, application_status, review_comment, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)''',
            (inv_uid, random.choice(ent_uids), tid, atype,
             random.choice(reasons), astatus, review_comment,
             random_date(1, 10), now))
        count += 1

conn.commit()
print(f'Created {count} applications')

# ====================== 10. 通知记录 ======================
print('\n=== 10. Creating notifications ===')

notif_types = [
    ('new_question', '您收到一个新的问题', '投资人对您的项目提出了问题'),
    ('question_answered', '问题已回答', '您提出的问题已获得回复'),
    ('new_application', '收到新的申请', '有投资人申请查看您的项目资料'),
    ('application_reviewed', '申请已审核', '您的申请已通过审核'),
    ('bp_request_approved', 'BP申请已批准', '您申请查看的商业计划书已获批准'),
    ('contact_request_approved', '联系方式申请已批准', '您申请的联系方式已获批准'),
    ('verification_approved', '实名认证已通过', '恭喜！您的实名认证已通过审核'),
    ('new_project', '新项目发布', '有新的项目发布了Teaser'),
    ('new_teaser', '新Teaser发布', '您关注的项目发布了新的Teaser'),
    ('system_announcement', '系统公告', '平台将于本周六进行系统维护升级'),
    ('ai_analysis_ready', 'AI分析完成', '您请求的投资分析报告已生成'),
]

all_uids = all_investor_uids + ent_uids
count = 0
for uid in random.sample(all_uids, min(10, len(all_uids))):
    for _ in range(random.randint(3, 8)):
        ntype, title, content = random.choice(notif_types)
        is_read = random.choice([True, True, False])
        cur.execute('''INSERT INTO notifications (user_id, notification_type, title, content,
            is_read, related_id, related_type, created_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)''',
            (uid, ntype, title, content, is_read,
             random.randint(1, 20), random.choice(['TEASER', 'PROJECT', 'QA', 'APPLICATION']),
             random_date(1, 20)))
        count += 1

conn.commit()
print(f'Created {count} notifications')

# ====================== 11. 用户角色关联 ======================
print('\n=== 11. Creating user-role associations ===')

# admin user -> admin role
cur.execute('SELECT id FROM roles WHERE role_code = %s', ('admin',))
admin_role = cur.fetchone()
if admin_role:
    cur.execute('INSERT IGNORE INTO user_roles (user_id, role_id, is_active, created_at) VALUES (12, %s, 1, %s)',
                (admin_role[0], now))
    print(f'  Assigned admin role to user 12')

# investor role
cur.execute('SELECT id FROM roles WHERE role_code = %s', ('investor',))
inv_role = cur.fetchone()
if inv_role:
    for uid in investor_uids:
        cur.execute('INSERT IGNORE INTO user_roles (user_id, role_id, is_active, created_at) VALUES (%s, %s, 1, %s)',
                    (uid, inv_role[0], now))
    print(f'  Assigned investor role to {len(investor_uids)} users')

# entrepreneur role
cur.execute('SELECT id FROM roles WHERE role_code = %s', ('entrepreneur',))
ent_role = cur.fetchone()
if ent_role:
    for uid in ent_uids:
        cur.execute('INSERT IGNORE INTO user_roles (user_id, role_id, is_active, created_at) VALUES (%s, %s, 1, %s)',
                    (uid, ent_role[0], now))
    print(f'  Assigned entrepreneur role to {len(ent_uids)} users')

conn.commit()

# ====================== 最终统计 ======================
print('\n' + '=' * 50)
print('=== Final Data Summary ===')
print('=' * 50)

tables = {
    'users': '用户',
    'investor_profiles': '投资人资料',
    'entrepreneur_profiles': '融资方资料',
    'projects': '项目',
    'teasers': 'Teaser',
    'favorites': '收藏',
    'view_histories': '浏览记录',
    'investment_analyses': '投资分析',
    'qa_records': '问答记录',
    'applications': '申请',
    'notifications': '通知',
    'user_roles': '用户角色',
}
for t, label in tables.items():
    try:
        cur.execute(f'SELECT COUNT(*) FROM {t}')
        cnt = cur.fetchone()[0]
        print(f'  {label} ({t}): {cnt} 条')
    except Exception as e:
        print(f'  {label} ({t}): ERROR - {e}')

conn.close()
print('\nAll test data inserted successfully!')
