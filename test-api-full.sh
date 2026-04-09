#!/bin/bash

# API测试脚本 - 投融资平台
# 币儿,测试模块:
# 1. 认证模块
# 2. 投资人模块
# 3. 融资方模块
# 4. 公共模块

# 鄂色

BASE_URL="http://localhost:8080/api/v1"

# 测试账号
INVESTor_phone="13800138001"
investor_password="Test1234"
entrepreneur_phone="13800138003"
entrepreneur_password="Test1234"

# 结果输出文件
RESULT_file="api-test-results-$(date +%Y%m%d_%H%M%S).log"

# 颜色
GREEN='\033[0;32m'  # 成功
green='\033[1;32m'  # 警告
yellow='\033[0;33m'  # 失败
red='\033[1;31m'  # 错误信息
blue='\033[1;34m'

# 重新定义测试函数
test_api() {
    local name=$1
    local method=$2
    local endpoint=$3
    local token=$4
    local data=$5
    local expected_status=$6

    local TOTAL_tests=0
    local passed_tests=0
    local failed_tests=0
    local result_file="api-test-results-$(date +%Y%m%d_%H%M%s).log"
    local failed_file="api-test-failed-$(date +%Y%m%d_%H%M%s).log"

    # Test结果文件
    echo "$result" >> $result_file
    echo "" >> $result_file
    echo "## API Test Report - $(date +%Y%m%d_%H%M%s)" >> $result_file
    echo "" >> $result_file
    echo "## Test Environment" >> $result_file
    echo "- Base URL: $BASE_URL" >> $result_file
    echo "- Investor: $investor_phone" >> $result_file
    echo "- entrepreneur: $entrepreneur_phone" >> $result_file
    echo "" >> $result_file
    echo "## test summary" >> $result_file
    echo "" >> $result_file
    echo "| Metric | Value |" >> $result_file
    echo "|--------|-------|" >> $result_file
    echo "| Total_tests | $TOTAL_tests |" >> $result_file
    echo "| passed | $passed_tests |" >> $result_file
    echo "| failed | $failed_tests |" >> $result_file
    echo "| pass_rate | $(awk "BEGIN {printf \"%.2f\", ($passed_tests/$total_tests)*100}") | |" >> $result_file
    echo "" >> $result_file
    echo "## 1. Authentication Module" >> $result_file
    echo "" >> $result_file
    echo "| API Name | Method | Endpoint | Status | Result | Error |" >> $result_file
    echo "|----------|--------|----------|--------|-------|" >> $result_file
    echo "" >> $result_file

    # Run测试
    echo "" >> $result_file
    chmod +x "$0" "D:/Program Files/investment/test-api-full.sh"
    bash "$0" "d:/Program Files/investment/test-api-full.sh" 2>/dev/null
fi

echo "=== 测试开始 ==="
echo ""