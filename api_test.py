#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
投融资平台 - 全面API自动化测试脚本
测试日期: 2026-04-09
数据依赖链: 登录 → 创建项目 → 创建并发布Teaser → 投资人操作(收藏/分析/问答/申请)
"""

import requests
import json
import sys
import io
from datetime import datetime

# 修复Windows控制台编码问题
sys.stdout.reconfigure(encoding='utf-8')

# 配置
BASE_URL = "http://localhost:8080/api/v1"
TEST_INVESTOR_PHONE = "13800138001"
TEST_INVESTOR_PASSWORD = "Test1234"
TEST_ENTREPRENEUR_PHONE = "13800138003"
TEST_ENTREPRENEUR_PASSWORD = "Test1234"
TEST_ADMIN_PHONE = "13800138000"
TEST_ADMIN_PASSWORD = "Test1234"

# 测试结果
results = {
    "total": 0,
    "passed": 0,
    "failed": 0,
    "details": []
}


def log(msg, level="INFO"):
    timestamp = datetime.now().strftime("%H:%M:%S")
    print(f"[{timestamp}] [{level}] {msg}")


def test_api(name, method, endpoint, headers=None, data=None, params=None,
             expected_code=200, token=None, is_json=True, accept_codes=None):
    """测试单个API

    Args:
        accept_codes: 额外可接受的响应code列表（如[30009]表示30009也视为通过）
    """
    results["total"] += 1
    url = f"{BASE_URL}{endpoint}"

    if headers is None:
        headers = {}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    if is_json and method in ("POST", "PUT", "PATCH"):
        headers.setdefault("Content-Type", "application/json")

    try:
        kwargs = {"headers": headers, "timeout": 15}
        if params:
            kwargs["params"] = params
        if data is not None and method in ("POST", "PUT", "PATCH"):
            if is_json:
                kwargs["json"] = data
            else:
                kwargs["data"] = data

        resp = requests.request(method, url, **kwargs)
        status = resp.status_code

        try:
            resp_data = resp.json()
            code = resp_data.get("code", status)
        except Exception:
            resp_data = {"raw": resp.text[:200]}
            code = status

        # 判断是否通过 (200 或 201 都算成功)
        accepted = [expected_code]
        if expected_code == 200:
            accepted.extend([200, 201])
        if accept_codes:
            accepted.extend(accept_codes)

        if code in accepted:
            results["passed"] += 1
            log(f"PASS | {name} | {method} {endpoint} | Status: {status}, Code: {code}", "PASS")
            return True, resp_data
        else:
            results["failed"] += 1
            log(f"FAIL | {name} | {method} {endpoint} | Expected: {expected_code}, Got: {code}", "FAIL")
            # 打印详细错误信息
            if isinstance(resp_data, dict):
                msg = resp_data.get("message", resp_data.get("msg", ""))
                if msg:
                    log(f"  -> Error: {msg[:200]}", "FAIL")
            results["details"].append({
                "name": name,
                "endpoint": endpoint,
                "method": method,
                "expected": expected_code,
                "actual": code,
                "response": resp_data
            })
            return False, resp_data

    except requests.exceptions.Timeout:
        results["failed"] += 1
        log(f"FAIL | {name} | Timeout", "ERROR")
        return False, {"error": "Timeout"}
    except Exception as e:
        results["failed"] += 1
        log(f"FAIL | {name} | Error: {str(e)}", "ERROR")
        return False, {"error": str(e)}


def extract_id(resp_data, field="id"):
    """从API响应中提取ID"""
    data = resp_data.get("data", {})
    if isinstance(data, dict):
        return data.get(field)
    return None


def extract_page_content(resp_data):
    """从Page响应中提取content列表"""
    data = resp_data.get("data", {})
    if isinstance(data, dict):
        return data.get("content", [])
    if isinstance(data, list):
        return data
    return []


def run_tests():
    """执行所有测试"""
    print("=" * 60)
    print("       投融资平台 - 全面API自动化测试")
    print("       测试时间: " + datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    print("=" * 60)

    # ==================== 1. 认证模块 ====================
    print("\n" + "=" * 60)
    print("模块1: 认证模块")
    print("=" * 60)

    # 1.1 投资人登录
    success, resp = test_api(
        "投资人登录", "POST", "/auth/login",
        data={"phone": TEST_INVESTOR_PHONE, "password": TEST_INVESTOR_PASSWORD}
    )
    investor_token = resp.get("data", {}).get("token", "") if success else ""
    investor_id = resp.get("data", {}).get("user", {}).get("id", "")

    # 1.2 融资方登录
    success, resp = test_api(
        "融资方登录", "POST", "/auth/login",
        data={"phone": TEST_ENTREPRENEUR_PHONE, "password": TEST_ENTREPRENEUR_PASSWORD}
    )
    entrepreneur_token = resp.get("data", {}).get("token", "") if success else ""

    # 1.3 刷新Token - 使用实际的refreshToken
    refresh_token = ""
    if success:
        refresh_token = resp.get("data", {}).get("refreshToken", "")
    if investor_token:
        login_resp = requests.post(f"{BASE_URL}/auth/login",
                                   json={"phone": TEST_INVESTOR_PHONE, "password": TEST_INVESTOR_PASSWORD},
                                   timeout=10).json()
        refresh_token = login_resp.get("data", {}).get("refreshToken", "")
    if refresh_token:
        test_api("刷新Token", "POST", "/auth/refresh",
                 data={"refreshToken": refresh_token})
    else:
        log("跳过刷新Token测试: 无法获取refreshToken", "WARN")

    # 1.4 管理员登录
    admin_success, admin_resp = test_api(
        "管理员登录", "POST", "/auth/login",
        data={"phone": TEST_ADMIN_PHONE, "password": TEST_ADMIN_PASSWORD}
    )
    admin_token = admin_resp.get("data", {}).get("token", "") if admin_success else ""

    # ==================== 2. 用户资料模块 ====================
    print("\n" + "=" * 60)
    print("模块2: 用户资料模块")
    print("=" * 60)

    test_api("获取用户资料", "GET", "/profile", token=investor_token)
    test_api("更新用户资料", "PUT", "/profile", token=investor_token,
             data={"nickname": "测试投资人", "bio": "测试简介"})
    test_api("获取投资人资料", "GET", "/profile/investor", token=investor_token)
    test_api("更新投资人资料", "PUT", "/profile/investor", token=investor_token,
             data={"institutionName": "测试投资机构", "position": "投资经理"})
    test_api("获取融资方资料", "GET", "/profile/entrepreneur", token=entrepreneur_token)
    test_api("更新融资方资料", "PUT", "/profile/entrepreneur", token=entrepreneur_token,
             data={"companyName": "测试科技公司", "industry": "AI"})
    # 实名认证 - 30009=已提交过认证也算通过（正确业务逻辑）
    test_api("实名认证", "POST", "/profile/verification", token=investor_token,
             data={
                 "realName": "张三",
                 "idCardNumber": "110101199001011234",
                 "idCardFrontUrl": "http://example.com/id_front.jpg",
                 "idCardBackUrl": "http://example.com/id_back.jpg",
                 "verificationType": "investor"
             }, accept_codes=[30009])
    test_api("获取隐私设置", "GET", "/profile/privacy", token=investor_token)
    test_api("更新隐私设置", "PUT", "/profile/privacy", token=investor_token,
             data={"allowShowCompanyName": True, "allowPublicQa": True})

    # ==================== 3. 融资方 - 项目管理 ====================
    print("\n" + "=" * 60)
    print("模块3: 融资方 - 项目管理 (创建测试数据)")
    print("=" * 60)

    # 3.1 创建项目
    proj_success, proj_resp = test_api(
        "创建项目", "POST", "/entrepreneur/projects", token=entrepreneur_token,
        data={
            "name": "API测试项目",
            "summary": "自动化测试创建的项目",
            "industry": "AI",
            "financingStage": "A",
            "financingAmount": 500,
            "financingPurpose": "用于产品研发",
            "location": "北京"
        }
    )
    project_id = extract_id(proj_resp) if proj_success else None

    # 3.2 获取项目列表
    test_api("获取项目列表", "GET", "/entrepreneur/projects", token=entrepreneur_token)

    # 3.3 获取项目详情
    if project_id:
        test_api("获取项目详情", "GET", f"/entrepreneur/projects/{project_id}", token=entrepreneur_token)

    # 3.4 更新项目
    if project_id:
        test_api("更新项目", "PUT", f"/entrepreneur/projects/{project_id}",
                 token=entrepreneur_token,
                 data={
                     "name": "更新后的测试项目",
                     "summary": "更新后的项目简介",
                     "industry": "AI",
                     "financingStage": "B",
                     "financingAmount": 1000
                 })

    # ==================== 4. 融资方 - Teaser管理 ====================
    print("\n" + "=" * 60)
    print("模块4: 融资方 - Teaser管理")
    print("=" * 60)

    published_teaser_id = None
    draft_teaser_id = None

    if project_id:
        # 4.1 创建Teaser(用于发布的)
        t1_success, t1_resp = test_api(
            "创建Teaser(待发布)", "POST", "/entrepreneur/teasers", token=entrepreneur_token,
            data={
                "projectId": project_id,
                "title": "API测试Teaser-待发布",
                "summary": "测试Teaser一句话介绍",
                "highlights": ["核心亮点1", "核心亮点2"],
                "businessModel": "B2B SaaS",
                "targetMarket": "企业级市场",
                "competitiveAdvantage": "技术领先"
            }
        )
        published_teaser_id = extract_id(t1_resp) if t1_success else None

        # 4.2 发布Teaser
        if published_teaser_id:
            test_api("发布Teaser", "POST",
                     f"/entrepreneur/teasers/{published_teaser_id}/publish",
                     token=entrepreneur_token)

        # 4.3 创建第二个项目用于草稿Teaser
        p2_success, p2_resp = test_api(
            "创建第二个项目(草稿Teaser)", "POST", "/entrepreneur/projects", token=entrepreneur_token,
            data={
                "name": "API测试项目2",
                "summary": "草稿Teaser用项目",
                "industry": "FINTECH",
                "financingStage": "SEED"
            }
        )
        project2_id = extract_id(p2_resp) if p2_success else None

        # 4.4 创建草稿Teaser(用于更新/删除测试)
        draft_teaser_id = None
        if project2_id:
            t2_success, t2_resp = test_api(
                "创建Teaser(草稿)", "POST", "/entrepreneur/teasers", token=entrepreneur_token,
                data={
                    "projectId": project2_id,
                    "title": "API测试Teaser-草稿",
                    "summary": "草稿Teaser简介"
                }
            )
            draft_teaser_id = extract_id(t2_resp) if t2_success else None

    # 4.4 获取Teaser列表
    test_api("获取Teaser列表", "GET", "/entrepreneur/teasers", token=entrepreneur_token)

    # 4.5 获取Teaser详情
    if published_teaser_id:
        test_api("获取Teaser详情", "GET",
                 f"/entrepreneur/teasers/{published_teaser_id}",
                 token=entrepreneur_token)

    # 4.6 更新草稿Teaser
    if draft_teaser_id:
        test_api("更新Teaser", "PUT", f"/entrepreneur/teasers/{draft_teaser_id}",
                 token=entrepreneur_token,
                 data={
                     "projectId": project_id,
                     "title": "更新后的草稿Teaser",
                     "summary": "更新后的简介"
                 })

    # 4.7 自动生成Teaser - 需要没有Teaser的项目
    # 跳过: 第一个项目已有Teaser, 自动生成会返回"Teaser已存在"
    # test_api("自动生成Teaser", "POST",
    #          f"/entrepreneur/teasers/auto-generate/{project_id}",
    #          token=entrepreneur_token)

    # ==================== 5. 投资人Dashboard模块 ====================
    print("\n" + "=" * 60)
    print("模块5: 投资人Dashboard模块")
    print("=" * 60)

    test_api("投资人Dashboard", "GET", "/investor/dashboard", token=investor_token)
    test_api("获取统计数据", "GET", "/investor/dashboard/stats", token=investor_token)
    test_api("获取推荐项目", "GET", "/investor/dashboard/recommended", token=investor_token)
    test_api("获取最近活动", "GET", "/investor/dashboard/activities", token=investor_token)

    # ==================== 6. Teaser搜索模块(投资人端) ====================
    print("\n" + "=" * 60)
    print("模块6: Teaser搜索模块")
    print("=" * 60)

    test_api("获取Teaser列表", "GET", "/investor/teasers", token=investor_token)
    test_api("搜索Teaser", "POST", "/investor/teasers/search", token=investor_token,
             data={"keyword": "AI", "page": 1, "pageSize": 10})
    if published_teaser_id:
        test_api("获取Teaser详情", "GET", f"/investor/teasers/{published_teaser_id}",
                 token=investor_token)
    else:
        test_api("获取Teaser详情", "GET", "/investor/teasers/1", token=investor_token)
    test_api("获取推荐Teaser", "GET", "/investor/teasers/recommended", token=investor_token)
    test_api("获取热门Teaser", "GET", "/investor/teasers/hot", token=investor_token)

    # ==================== 7. 投资人收藏模块 ====================
    print("\n" + "=" * 60)
    print("模块7: 投资人收藏模块")
    print("=" * 60)

    test_api("获取收藏列表", "GET", "/investor/favorites", token=investor_token)

    # 添加收藏(使用发布的Teaser)
    if published_teaser_id:
        test_api("添加收藏", "POST", "/investor/favorites", token=investor_token,
                 data={"teaserId": published_teaser_id, "groupName": "测试分组", "note": "测试收藏备注"})
        test_api("检查是否已收藏", "GET", f"/investor/favorites/check/{published_teaser_id}",
                 token=investor_token)
        test_api("更新收藏分组", "PUT", f"/investor/favorites/{published_teaser_id}/group",
                 token=investor_token,
                 data={"groupName": "重要项目"})
        test_api("获取收藏分组列表", "GET", "/investor/favorites/groups", token=investor_token)
        test_api("删除收藏", "DELETE", f"/investor/favorites/{published_teaser_id}",
                 token=investor_token)
    else:
        log("跳过收藏操作: 没有可用的已发布Teaser", "WARN")

    # ==================== 8. 投资分析模块 ====================
    print("\n" + "=" * 60)
    print("模块8: 投资分析模块")
    print("=" * 60)

    test_api("获取分析列表", "GET", "/investor/analysis", token=investor_token)

    analysis_id = None
    if published_teaser_id:
        a_success, a_resp = test_api(
            "创建投资分析", "POST", "/investor/analysis", token=investor_token,
            data={"teaserId": published_teaser_id}
        )
        analysis_id = extract_id(a_resp) if a_success else None

        if analysis_id:
            test_api("获取分析详情", "GET", f"/investor/analysis/{analysis_id}",
                     token=investor_token)
        test_api("获取Teaser的分析", "GET", f"/investor/analysis/teaser/{published_teaser_id}",
                 token=investor_token)
        if analysis_id:
            test_api("重新分析", "POST", f"/investor/analysis/{analysis_id}/reanalyze",
                     token=investor_token)
            test_api("删除分析", "DELETE", f"/investor/analysis/{analysis_id}",
                     token=investor_token)
    else:
        log("跳过分析操作: 没有可用的已发布Teaser", "WARN")

    # ==================== 9. 投资人问答模块 ====================
    print("\n" + "=" * 60)
    print("模块9: 投资人问答模块")
    print("=" * 60)

    test_api("获取问答列表", "GET", "/investor/qa", token=investor_token)

    qa_id = None
    if published_teaser_id:
        qa_success, qa_resp = test_api(
            "发送问题", "POST", "/investor/qa", token=investor_token,
            data={"teaserId": published_teaser_id, "question": "这个项目的竞争优势是什么？"}
        )
        qa_id = extract_id(qa_resp) if qa_success else None

        if qa_id:
            test_api("获取问题详情(投资人)", "GET", f"/investor/qa/{qa_id}",
                     token=investor_token)
    else:
        log("跳过问答操作: 没有可用的已发布Teaser", "WARN")

    # ==================== 10. 投资人申请模块 ====================
    print("\n" + "=" * 60)
    print("模块10: 投资人申请模块")
    print("=" * 60)

    test_api("获取申请列表", "GET", "/investor/applications", token=investor_token)

    application_id = None
    if published_teaser_id:
        app_success, app_resp = test_api(
            "创建申请", "POST", "/investor/applications", token=investor_token,
            data={
                "teaserId": published_teaser_id,
                "applicationType": "GET_BP",
                "reason": "希望了解项目详细信息"
            }
        )
        application_id = extract_id(app_resp) if app_success else None

        if application_id:
            test_api("获取申请详情(投资人)", "GET", f"/investor/applications/{application_id}",
                     token=investor_token)
    else:
        log("跳过申请操作: 没有可用的已发布Teaser", "WARN")

    # ==================== 11. 融资方Dashboard模块 ====================
    print("\n" + "=" * 60)
    print("模块11: 融资方Dashboard模块")
    print("=" * 60)

    test_api("融资方Dashboard", "GET", "/entrepreneur/dashboard", token=entrepreneur_token)

    # ==================== 12. 融资方问答模块 ====================
    print("\n" + "=" * 60)
    print("模块12: 融资方问答模块")
    print("=" * 60)

    test_api("获取收到的问题列表", "GET", "/entrepreneur/qa", token=entrepreneur_token)

    if qa_id:
        test_api("获取问题详情(融资方)", "GET", f"/entrepreneur/qa/{qa_id}",
                 token=entrepreneur_token)
        test_api("回答问题", "POST", f"/entrepreneur/qa/{qa_id}/answer",
                 token=entrepreneur_token,
                 data={"answer": "我们的核心竞争优势是AI算法的技术壁垒和先发优势。"})
    else:
        log("跳过融资方问答操作: 没有可用的问答ID", "WARN")

    # ==================== 13. 融资方申请模块 ====================
    print("\n" + "=" * 60)
    print("模块13: 融资方申请模块")
    print("=" * 60)

    test_api("获取申请列表(融资方)", "GET", "/entrepreneur/applications",
             token=entrepreneur_token)

    if application_id:
        test_api("获取申请详情(融资方)", "GET",
                 f"/entrepreneur/applications/{application_id}",
                 token=entrepreneur_token)
        # 审核申请 - 注意: 使用@RequestParam, 通过查询参数传递
        test_api("审核申请", "POST",
                 f"/entrepreneur/applications/{application_id}/review",
                 token=entrepreneur_token,
                 params={"approved": "true", "comment": "审核通过"},
                 data=None)
    else:
        log("跳过融资方申请操作: 没有可用的申请ID", "WARN")

    # ==================== 14. 商业计划书模块 ====================
    print("\n" + "=" * 60)
    print("模块14: 商业计划书模块")
    print("=" * 60)

    if project_id:
        test_api("获取项目BP列表", "GET", f"/entrepreneur/bp/project/{project_id}",
                 token=entrepreneur_token)
    else:
        test_api("获取项目BP列表", "GET", "/entrepreneur/bp/project/1",
                 token=entrepreneur_token)

    test_api("获取BP详情(不存在)", "GET", "/entrepreneur/bp/1", token=entrepreneur_token,
             expected_code=40016)
    test_api("删除BP(不存在)", "DELETE", "/entrepreneur/bp/99999",
             token=entrepreneur_token, expected_code=40016)

    # ==================== 15. 通知模块 ====================
    print("\n" + "=" * 60)
    print("模块15: 通知模块")
    print("=" * 60)

    test_api("获取通知列表", "GET", "/notifications", token=investor_token)
    test_api("获取未读通知", "GET", "/notifications/unread", token=investor_token)
    test_api("获取未读数量", "GET", "/notifications/unread/count", token=investor_token)

    # 从通知列表中提取通知ID
    notif_success, notif_resp = test_api("获取通知列表(提取ID)", "GET", "/notifications",
                                          token=investor_token)
    notif_id = None
    if notif_success:
        content = extract_page_content(notif_resp)
        if content and len(content) > 0:
            notif_id = content[0].get("id")

    if notif_id:
        test_api("标记已读", "PUT", f"/notifications/{notif_id}/read",
                 token=investor_token)
    else:
        log("跳过通知标记已读: 没有可用的通知ID", "WARN")

    test_api("全部标记已读", "PUT", "/notifications/read-all", token=investor_token)

    # ==================== 16. 后台管理模块 ====================
    print("\n" + "=" * 60)
    print("模块16: 后台管理模块")
    print("=" * 60)

    if admin_token:
        test_api("管理员Dashboard", "GET", "/admin/dashboard", token=admin_token)
        test_api("获取用户列表", "GET", "/admin/users", token=admin_token)

        # 更新用户状态(使用投资人ID)
        if investor_id:
            test_api("更新用户状态", "PUT", f"/admin/users/{investor_id}/status",
                     token=admin_token,
                     data={"status": "ACTIVE", "reason": "自动化测试-恢复正常"})
        else:
            test_api("更新用户状态", "PUT", "/admin/users/1/status",
                     token=admin_token,
                     data={"status": "ACTIVE", "reason": "自动化测试"})

        test_api("获取认证列表", "GET", "/admin/verifications", token=admin_token)
        # 从认证列表中获取认证ID
        verif_success, verif_resp = test_api("获取认证列表(提取ID)", "GET",
                                              "/admin/verifications", token=admin_token)
        verif_id = None
        if verif_success:
            verif_content = extract_page_content(verif_resp)
            if verif_content and len(verif_content) > 0:
                verif_id = verif_content[0].get("id")
        if verif_id:
            test_api("获取认证详情", "GET", f"/admin/verifications/{verif_id}",
                     token=admin_token)
            test_api("审核认证", "POST", f"/admin/verifications/{verif_id}/review",
                     token=admin_token,
                     data={"approved": True, "comment": "审核通过"})
        else:
            log("跳过认证审核: 没有可用的认证记录", "WARN")
    else:
        log("管理员登录失败，跳过管理员模块测试", "WARN")

    # ==================== 17. 文件上传模块 ====================
    print("\n" + "=" * 60)
    print("模块17: 文件上传模块 (依赖服务端uploads目录)")
    print("=" * 60)

    for upload_name, endpoint, filename, content_type, content in [
        ("上传图片", "/common/upload/image", "test.jpg", "image/jpeg",
         b'\xff\xd8\xff\xe0' + b'\x00' * 100),  # JPEG magic bytes + padding
        ("上传文档", "/common/upload/document", "test.pdf", "application/pdf",
         b'%PDF-1.4\n%test content\n' + b'\x00' * 100),  # PDF magic bytes
        ("上传头像", "/common/upload/avatar", "avatar.jpg", "image/jpeg",
         b'\xff\xd8\xff\xe0' + b'\x00' * 50),
    ]:
        try:
            files = {"file": (filename, io.BytesIO(content), content_type)}
            headers = {"Authorization": f"Bearer {investor_token}"}
            resp = requests.post(f"{BASE_URL}{endpoint}", headers=headers,
                                files=files, timeout=15)
            resp_data = resp.json()
            code = resp_data.get("code", resp.status_code)
            results["total"] += 1
            if code in [200, 201]:
                results["passed"] += 1
                log(f"PASS | {upload_name} | POST {endpoint} | Code: {code}", "PASS")
            else:
                results["failed"] += 1
                log(f"FAIL | {upload_name} | POST {endpoint} | Expected: 200, Got: {code}", "FAIL")
                results["details"].append({
                    "name": upload_name, "endpoint": endpoint, "method": "POST",
                    "expected": 200, "actual": code, "response": resp_data
                })
        except Exception as e:
            results["total"] += 1
            results["failed"] += 1
            log(f"FAIL | {upload_name} | Error: {str(e)}", "ERROR")

    # ==================== 清理测试数据 ====================
    print("\n" + "=" * 60)
    print("模块18: 清理测试数据")
    print("=" * 60)

    if draft_teaser_id:
        test_api("删除草稿Teaser", "DELETE", f"/entrepreneur/teasers/{draft_teaser_id}",
                 token=entrepreneur_token)
    if published_teaser_id:
        # 先下架再删除
        test_api("下架Teaser", "POST", f"/entrepreneur/teasers/{published_teaser_id}/unpublish",
                 token=entrepreneur_token)
        test_api("删除已发布Teaser", "DELETE", f"/entrepreneur/teasers/{published_teaser_id}",
                 token=entrepreneur_token)
    if project_id:
        test_api("删除测试项目", "DELETE", f"/entrepreneur/projects/{project_id}",
                 token=entrepreneur_token)
    if project2_id:
        test_api("删除第二个项目", "DELETE", f"/entrepreneur/projects/{project2_id}",
                 token=entrepreneur_token)

    # ==================== 输出测试报告 ====================
    print("\n" + "=" * 60)
    print("              测试结果汇总")
    print("=" * 60)
    print(f"\n总测试数: {results['total']}")
    print(f"通过: {results['passed']} PASS")
    print(f"失败: {results['failed']} FAIL")

    if results['total'] > 0:
        pass_rate = (results['passed'] / results['total']) * 100
        print(f"通过率: {pass_rate:.1f}%")

    if results['failed'] > 0:
        print("\n" + "-" * 60)
        print("失败的API详情:")
        print("-" * 60)
        for detail in results['details']:
            print(f"  - {detail['name']}: {detail['method']} {detail['endpoint']}")
            print(f"    期望: {detail['expected']}, 实际: {detail['actual']}")
            if isinstance(detail.get('response'), dict):
                msg = detail['response'].get('message', detail['response'].get('msg', ''))
                if msg:
                    print(f"    错误信息: {str(msg)[:200]}")

    print("\n" + "=" * 60)
    print("              测试完成!")
    print("=" * 60)

    # 保存报告到文件
    report = {
        "timestamp": datetime.now().isoformat(),
        "summary": {
            "total": results['total'],
            "passed": results['passed'],
            "failed": results['failed'],
            "pass_rate": f"{(results['passed'] / results['total'] * 100):.1f}%" if results['total'] > 0 else "0%"
        },
        "failed_details": results['details']
    }

    with open("test_report.json", "w", encoding="utf-8") as f:
        json.dump(report, f, ensure_ascii=False, indent=2)

    print(f"\n测试报告已保存到: test_report.json")

    return results


if __name__ == "__main__":
    run_tests()
