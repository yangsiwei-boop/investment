#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
初始化测试数据脚本
"""

import mysql.connector
import sys

# 修复Windows控制台编码问题
sys.stdout.reconfigure(encoding='utf-8')

# 数据库配置
DB_CONFIG = {
    'host': '36.150.231.203',
    'port': 3306,
    'user': 'root',
    'password': 'root_PASS866.',
    'database': 'investment',
    'charset': 'utf8mb4'
}

def execute_sql():
    """执行SQL初始化数据"""
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()

        print("=" * 60)
        print("开始初始化测试数据...")
        print("=" * 60)

        # 1. 创建管理员账号
        print("\n1. 创建管理员账号...")
        admin_sql = """
        INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
        VALUES ('13800138000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'admin@investment.com', 'ADMIN', 'ACTIVE', 1, NOW(), NOW())
        ON DUPLICATE KEY UPDATE
            password_hash = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM',
            status = 'ACTIVE',
            is_verified = 1,
            updated_at = NOW()
        """
        cursor.execute(admin_sql)
        print("   ✓ 管理员账号已创建: 13800138000 / Admin1234")

        # 2. 确保投资人账号存在并更新
        print("\n2. 更新投资人测试账号...")
        investor_sql = """
        INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
        VALUES ('13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'investor@test.com', 'INVESTOR', 'ACTIVE', 1, NOW(), NOW())
        ON DUPLICATE KEY UPDATE status = 'ACTIVE', is_verified = 1, updated_at = NOW()
        """
        cursor.execute(investor_sql)
        print("   ✓ 投资人账号已更新: 13800138001 / Test1234")

        # 3. 确保融资方账号存在并更新
        print("\n3. 更新融资方测试账号...")
        entrepreneur_sql = """
        INSERT INTO users (phone, password_hash, email, user_type, status, is_verified, created_at, updated_at)
        VALUES ('13800138003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHdM1dfSv8bh4n9qZ5EHdM', 'entrepreneur@test.com', 'ENTREPRENEUR', 'ACTIVE', 1, NOW(), NOW())
        ON DUPLICATE KEY UPDATE status = 'ACTIVE', is_verified = 1, updated_at = NOW()
        """
        cursor.execute(entrepreneur_sql)
        print("   ✓ 融资方账号已更新: 13800138003 / Test1234")

        # 获取融资方用户ID
        cursor.execute("SELECT id FROM users WHERE phone = '13800138003'")
        entrepreneur_id = cursor.fetchone()[0]
        print(f"   融资方用户ID: {entrepreneur_id}")

        # 获取投资人用户ID
        cursor.execute("SELECT id FROM users WHERE phone = '13800138001'")
        investor_id = cursor.fetchone()[0]
        print(f"   投资人用户ID: {investor_id}")

        # 4. 创建投资人资料
        print("\n4. 创建投资人资料...")
        investor_profile_sql = f"""
        INSERT INTO investor_profiles (user_id, institution_name, created_at, updated_at)
        VALUES ({investor_id}, '测试投资机构', NOW(), NOW())
        ON DUPLICATE KEY UPDATE institution_name = '测试投资机构', updated_at = NOW()
        """
        cursor.execute(investor_profile_sql)
        print("   ✓ 投资人资料已创建")

        # 5. 创建融资方资料
        print("\n5. 创建融资方资料...")
        entrepreneur_profile_sql = f"""
        INSERT INTO entrepreneur_profiles (user_id, company_name, created_at, updated_at)
        VALUES ({entrepreneur_id}, '测试融资公司', NOW(), NOW())
        ON DUPLICATE KEY UPDATE company_name = '测试融资公司', updated_at = NOW()
        """
        cursor.execute(entrepreneur_profile_sql)
        print("   ✓ 融资方资料已创建")

        # 6. 创建测试项目 - 使用正确的字段名
        print("\n6. 创建测试项目...")
        project_sql = f"""
        INSERT INTO projects (entrepreneur_user_id, project_name, industry, financing_stage, one_line_description, status, created_at, updated_at)
        VALUES ({entrepreneur_id}, 'AI智能投资平台', 'ai', 'a', '基于人工智能的投资决策辅助平台', 'published', NOW(), NOW())
        """
        cursor.execute(project_sql)
        project_id = cursor.lastrowid
        print(f"   ✓ 测试项目已创建, ID: {project_id}")

        # 7. 创建测试Teaser
        print("\n7. 创建测试Teaser...")
        teaser_sql = f"""
        INSERT INTO teasers (project_id, title, subtitle, status, view_count, created_at, updated_at)
        VALUES ({project_id}, 'AI投资平台Teaser', '领先的AI驱动投资决策平台', 'PUBLISHED', 100, NOW(), NOW())
        """
        cursor.execute(teaser_sql)
        teaser_id = cursor.lastrowid
        print(f"   ✓ 测试Teaser已创建, ID: {teaser_id}")

        # 提交事务
        conn.commit()

        # 验证数据
        print("\n" + "=" * 60)
        print("验证创建的数据:")
        print("=" * 60)

        cursor.execute("SELECT id, phone, user_type, status FROM users WHERE phone IN ('13800138000', '13800138001', '13800138003')")
        users = cursor.fetchall()
        print("\n用户数据:")
        for user in users:
            print(f"   ID: {user[0]}, Phone: {user[1]}, Type: {user[2]}, Status: {user[3]}")

        cursor.execute(f"SELECT id, project_name, status FROM projects WHERE entrepreneur_user_id = {entrepreneur_id}")
        projects = cursor.fetchall()
        print("\n项目数据:")
        for project in projects:
            print(f"   ID: {project[0]}, Name: {project[1]}, Status: {project[2]}")

        cursor.execute("SELECT id, title, status FROM teasers ORDER BY id DESC LIMIT 3")
        teasers = cursor.fetchall()
        print("\nTeaser数据:")
        for teaser in teasers:
            print(f"   ID: {teaser[0]}, Title: {teaser[1]}, Status: {teaser[2]}")

        print("\n" + "=" * 60)
        print("✅ 测试数据初始化完成!")
        print("=" * 60)

        cursor.close()
        conn.close()

        return True

    except mysql.connector.Error as e:
        print(f"❌ 数据库错误: {e}")
        return False
    except Exception as e:
        print(f"❌ 错误: {e}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    success = execute_sql()
    sys.exit(0 if success else 1)
