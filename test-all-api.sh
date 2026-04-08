#!/bin/bash

# 投融资对接平台 API 全面测试脚本
BASE_URL="http://localhost:8080/api"
TOKEN=""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试计数器
TOTAL=0
PASSED=0
FAILED=0

# 测试函数
test_api() {
    local name="$1"
    local method="$2"
    local endpoint="$3"
    local data="$4"
    local auth="$5"

    TOTAL=$((TOTAL + 1))
    echo -e "\n${YELLOW}测试 $TOTAL: $name${NC}"
    echo "请求: $method $endpoint"

    if [ -n "$data" ]; then
        echo "数据: $data"
    fi

    local headers="-H 'Content-Type: application/json'"
    if [ -n "$auth" ] && [ -n "$TOKEN" ]; then
        headers="$headers -H 'Authorization: Bearer $TOKEN'"
    fi

    local response
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL$endpoint" $headers 2>/dev/null)
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL$endpoint" $headers -d "$data" 2>/dev/null)
    elif [ "$method" = "PUT" ]; then
        response=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL$endpoint" $headers -d "$data" 2>/dev/null)
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL$endpoint" $headers 2>/dev/null)
    fi

    local http_code=$(echo "$response" | tail -n 1)
    local body=$(echo "$response" | sed '$d')

    echo "状态码: $http_code"
    echo "响应: $body"

    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}✓ 通过${NC}"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}✗ 失败${NC}"
        FAILED=$((FAILED + 1))
    fi
}

echo "=========================================="
echo "  投融资对接平台 API 全面测试"
echo "=========================================="

# ==================== 1. 认证模块测试 ====================
echo -e "\n========== 1. 认证模块测试 =========="

# 1.1 用户注册 - 投资人
test_api "投资人注册" "POST" "/auth/register" '{
    "username": "investor_test",
    "password": "Test123456",
    "email": "investor@test.com",
    "phone": "13800000001",
    "userType": "INVESTOR",
    "realName": "测试投资人"
}'

# 1.2 用户注册 - 融资用户
test_api "融资用户注册" "POST" "/auth/register" '{
    "username": "entrepreneur_test",
    "password": "Test123456",
    "email": "entrepreneur@test.com",
    "phone": "13800000002",
    "userType": "ENTREPRENEUR",
    "realName": "测试融资用户"
}'

# 1.3 用户登录
echo -e "\n--- 投资人登录获取Token ---"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username": "investor_test", "password": "Test123456"}' 2>/dev/null)
echo "登录响应: $LOGIN_RESPONSE"
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "Token: ${TOKEN:0:50}..."

# ==================== 2. 投资人模块测试 ====================
echo -e "\n========== 2. 投资人模块测试 =========="

# 2.1 获取当前用户信息
test_api "获取当前用户信息" "GET" "/users/me" "" "auth"

# 2.2 更新用户资料
test_api "更新投资人资料" "PUT" "/investors/profile" '{
    "companyName": "测试投资公司",
    "position": "投资经理",
    "investorType": "INDIVIDUAL",
    "investmentFields": ["互联网", "金融科技"],
    "investmentStage": ["A轮", "B轮"],
    "minInvestmentAmount": 1000000,
    "maxInvestmentAmount": 10000000
}' "auth"

# 2.3 获取投资人工作台数据
test_api "获取投资人工作台" "GET" "/investors/dashboard" "" "auth"

# 2.4 获取Teaser列表
test_api "获取Teaser列表" "GET" "/investors/teasers?page=1&size=10" "" "auth"

# 2.5 搜索Teaser
test_api "搜索Teaser" "GET" "/investors/teasers/search?keyword=测试&page=1&size=10" "" "auth"

# ==================== 3. 收藏功能测试 ====================
echo -e "\n========== 3. 收藏功能测试 =========="

# 3.1 添加收藏
test_api "添加收藏" "POST" "/investors/favorites" '{
    "teaserId": 1,
    "groupName": "重点关注",
    "note": "非常有潜力的项目"
}' "auth"

# 3.2 获取收藏列表
test_api "获取收藏列表" "GET" "/investors/favorites?page=1&size=10" "" "auth"

# 3.3 获取收藏分组列表 (重点测试)
test_api "获取收藏分组列表" "GET" "/investors/favorites/groups" "" "auth"

# 3.4 检查是否已收藏
test_api "检查是否已收藏" "GET" "/investors/favorites/check/1" "" "auth"

# ==================== 4. AI分析功能测试 ====================
echo -e "\n========== 4. AI分析功能测试 =========="

# 4.1 获取AI分析
test_api "获取AI分析" "GET" "/investors/analysis/teaser/1" "" "auth"

# 4.2 获取分析历史
test_api "获取分析历史" "GET" "/investors/analysis/history?page=1&size=10" "" "auth"

# ==================== 5. 问答功能测试 ====================
echo -e "\n========== 5. 问答功能测试 =========="

# 5.1 发送问题
test_api "发送问题" "POST" "/investors/questions" '{
    "teaserId": 1,
    "question": "请问贵公司的商业模式是什么？",
    "questionType": "BUSINESS"
}' "auth"

# 5.2 获取问题列表
test_api "获取问题列表" "GET" "/investors/questions?page=1&size=10" "" "auth"

# 5.3 获取问题库
test_api "获取问题库" "GET" "/investors/question-library" "" "auth"

# ==================== 6. 申请功能测试 ====================
echo -e "\n========== 6. 申请功能测试 =========="

# 6.1 提交BP申请
test_api "提交BP申请" "POST" "/investors/applications/bp" '{
    "teaserId": 1,
    "reason": "对项目非常感兴趣，希望获取详细BP"
}' "auth"

# 6.2 提交联系方式申请
test_api "提交联系方式申请" "POST" "/investors/applications/contact" '{
    "teaserId": 1,
    "reason": "希望进一步沟通合作细节"
}' "auth"

# 6.3 获取申请列表
test_api "获取申请列表" "GET" "/investors/applications?page=1&size=10" "" "auth"

# ==================== 7. 融资用户模块测试 ====================
echo -e "\n========== 7. 融资用户模块测试 =========="

# 切换到融资用户
echo -e "\n--- 融资用户登录 ---"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username": "entrepreneur_test", "password": "Test123456"}' 2>/dev/null)
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "Token: ${TOKEN:0:50}..."

# 7.1 获取融资用户工作台
test_api "获取融资用户工作台" "GET" "/entrepreneurs/dashboard" "" "auth"

# 7.2 更新融资用户资料
test_api "更新融资用户资料" "PUT" "/entrepreneurs/profile" '{
    "companyName": "测试科技有限公司",
    "industry": "INTERNET",
    "location": "北京市",
    "introduction": "一家专注于互联网创新的科技公司"
}' "auth"

# 7.3 创建项目
test_api "创建项目" "POST" "/entrepreneurs/projects" '{
    "projectName": "测试项目",
    "industry": "INTERNET",
    "financingStage": "SERIES_A",
    "financingAmount": 5000000,
    "description": "这是一个测试项目"
}' "auth"

# 7.4 获取项目列表
test_api "获取项目列表" "GET" "/entrepreneurs/projects?page=1&size=10" "" "auth"

# ==================== 8. 通知功能测试 ====================
echo -e "\n========== 8. 通知功能测试 =========="

test_api "获取通知列表" "GET" "/notifications?page=1&size=10" "" "auth"

test_api "获取未读通知数量" "GET" "/notifications/unread-count" "" "auth"

# ==================== 9. 通用功能测试 ====================
echo -e "\n========== 9. 通用功能测试 =========="

test_api "获取行业列表" "GET" "/common/industries" "" ""

test_api "获取融资阶段列表" "GET" "/common/financing-stages" "" ""

# ==================== 10. 后台管理测试 ====================
echo -e "\n========== 10. 后台管理测试 =========="

test_api "获取统计数据" "GET" "/admin/statistics" "" "auth"

test_api "获取用户列表" "GET" "/admin/users?page=1&size=10" "" "auth"

# ==================== 测试结果汇总 ====================
echo -e "\n=========================================="
echo "  测试结果汇总"
echo "=========================================="
echo -e "总计测试: $TOTAL"
echo -e "${GREEN}通过: $PASSED${NC}"
echo -e "${RED}失败: $FAILED${NC}"
echo -e "通过率: $(awk "BEGIN {printf \"%.1f\", ($PASSED/$TOTAL)*100}")%"
echo "=========================================="
