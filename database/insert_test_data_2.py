#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""补充测试数据 - 第二部分（分析、问答、申请、通知、角色关联）"""

import pymysql
import sys
import random
from datetime import datetime, timedelta

sys.stdout.reconfigure(encoding='utf-8')

conn = pymysql.connect(
    host='36.150.231.203', port=3306,
    user='root', password='root_PASS866.',
    database='investment', charset='utf8mb4', autocommit=False
)
cur = conn.cursor()
now = datetime.now()

def random_date(days_ago_min, days_ago_max):
    return now - timedelta(days=random.randint(days_ago_min, days_ago_max))

# Get created user IDs
cur.execute("SELECT id FROM users WHERE user_type='INVESTOR' AND id >= 22 ORDER BY id")
investor_uids = [r[0] for r in cur.fetchall()]
cur.execute("SELECT id FROM users WHERE user_type='ENTREPRENEUR' AND id >= 50 ORDER BY id")
ent_uids = [r[0] for r in cur.fetchall()]
cur.execute("SELECT id FROM teasers WHERE status='published' ORDER BY id DESC LIMIT 10")
teaser_ids = [r[0] for r in cur.fetchall()]
cur.execute("SELECT id FROM projects ORDER BY id DESC LIMIT 20")
project_ids = [r[0] for r in cur.fetchall()]

print(f'Investors: {len(investor_uids)}, Entrepreneurs: {len(ent_uids)}')
print(f'Teasers: {len(teaser_ids)}, Projects: {len(project_ids)}')

# ====================== 7. 投资分析 ======================
print('\n=== 7. Creating investment analyses ===')

analysis_types = ['comprehensive', 'market', 'team', 'financial', 'competitive']
recommendations = ['强烈推荐', '推荐', '建议关注', '谨慎评估']
verdicts = ['strong_buy', 'buy', 'hold', 'cautious']
count = 0
for inv_uid in investor_uids[:5]:
    for tid in random.sample(teaser_ids, min(random.randint(1, 3), len(teaser_ids))):
        atype = random.choice(analysis_types)
        score_val = round(random.uniform(60, 95), 1)
        rec = random.choice(recommendations)
        verdict = random.choice(verdicts)
        content = '{{"summary":"该项目在{}方面表现优秀","score":{}}}'.format(atype, score_val)
        cur.execute('''INSERT INTO investment_analyses (investor_user_id, teaser_id, analysis_type, analysis_content,
            overall_score, overall_verdict, score, recommendation, investment_suggestion,
            investment_highlights, risk_warnings, is_ai_generated, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, 1, %s, %s)''',
            (inv_uid, tid, atype, content,
             int(score_val), verdict, score_val, rec,
             rec + '投资',
             '技术壁垒高;市场空间大;团队背景强',
             '竞争加剧;政策变化风险',
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
    '目前市场处于早期阶段，我们的差异化在于技术领先和服务深度。',
    '核心团队由来自BAT的资深工程师和行业专家组成，平均从业经验超过10年。',
    '主要通过SaaS订阅和增值服务盈利，目前客单价XX万，毛利率达到70%。',
    '未来12个月我们计划完成新一轮融资，拓展到5个新城市，团队规模翻倍。',
    '目前CAC约为5000元，LTV约为5万元，LTV/CAC比达到10:1。',
    '我们计划在Q3推出2.0版本，新增AI辅助决策功能，Q4开放API平台。',
    '目前在我们专注的细分市场占有率约15%，季度增长率保持20%以上。',
    '计划将40%用于研发，30%用于市场拓展，20%用于团队建设，10%作为运营储备。',
    '我们计划在3-5年内实现IPO，同时也会考虑战略并购的可能性。',
]

count = 0
for inv_uid in random.sample(investor_uids, min(4, len(investor_uids))):
    for tid in random.sample(teaser_ids, min(random.randint(2, 4), len(teaser_ids))):
        qi = random.randint(0, len(questions) - 1)
        question = questions[qi]
        answer = answers[qi] if random.random() > 0.3 else None
        status = 'answered' if answer else random.choice(['pending', 'pending', 'ignored'])
        is_public = random.choice([True, True, False])
        answered_at = random_date(1, 10) if answer else None

        cur.execute('''INSERT INTO qa_records (investor_user_id, entrepreneur_user_id, project_id,
            question, answer, question_status, is_public, sent_at, answered_at, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)''',
            (inv_uid, random.choice(ent_uids), random.choice(project_ids),
             question, answer, status, is_public,
             random_date(3, 15), answered_at,
             random_date(3, 15), now))
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
for inv_uid in random.sample(investor_uids, min(5, len(investor_uids))):
    for tid in random.sample(teaser_ids, min(random.randint(1, 3), len(teaser_ids))):
        atype = random.choice(app_types)
        astatus = random.choice(app_statuses)
        reviewed_by = random.choice(ent_uids) if astatus != 'pending' else None
        reviewed_at_val = random_date(1, 5) if astatus != 'pending' else None

        cur.execute('''INSERT INTO applications (investor_user_id, entrepreneur_user_id, project_id, teaser_id,
            application_type, application_reason, application_status, reviewed_by, reviewed_at, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)''',
            (inv_uid, random.choice(ent_uids), random.choice(project_ids), tid, atype,
             random.choice(reasons), astatus, reviewed_by, reviewed_at_val,
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

all_uids = investor_uids + ent_uids
count = 0
for uid in random.sample(all_uids, min(10, len(all_uids))):
    for _ in range(random.randint(3, 8)):
        ntype, title, content = random.choice(notif_types)
        is_read = random.choice([True, True, False])
        cur.execute('''INSERT INTO notifications (user_id, notification_type, title, content,
            is_read, related_id, related_type, created_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)''',
            (uid, ntype, title, content, is_read,
             random.randint(1, 25), random.choice(['TEASER', 'PROJECT', 'QA', 'APPLICATION']),
             random_date(1, 20)))
        count += 1
conn.commit()
print(f'Created {count} notifications')

# ====================== 11. 用户角色关联 ======================
print('\n=== 11. Creating user-role associations ===')

cur.execute('SELECT id FROM roles WHERE role_code = %s', ('admin',))
admin_role = cur.fetchone()
if admin_role:
    cur.execute('INSERT IGNORE INTO user_roles (user_id, role_id, is_active, created_at) VALUES (12, %s, 1, %s)',
                (admin_role[0], now))
    print(f'  Assigned admin role to user 12')

cur.execute('SELECT id FROM roles WHERE role_code = %s', ('investor',))
inv_role = cur.fetchone()
if inv_role:
    for uid in investor_uids:
        cur.execute('INSERT IGNORE INTO user_roles (user_id, role_id, is_active, created_at) VALUES (%s, %s, 1, %s)',
                    (uid, inv_role[0], now))
    print(f'  Assigned investor role to {len(investor_uids)} users')

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
    'users': '用户', 'investor_profiles': '投资人资料', 'entrepreneur_profiles': '融资方资料',
    'projects': '项目', 'teasers': 'Teaser', 'favorites': '收藏', 'view_histories': '浏览记录',
    'investment_analyses': '投资分析', 'qa_records': '问答记录', 'applications': '申请',
    'notifications': '通知', 'user_roles': '用户角色',
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
