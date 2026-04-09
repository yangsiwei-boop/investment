# 投融资平台 API测试分析报告

**测试时间**: 2026-04-08 19:57
**总测试数**: 78
**通过数**: 25 (32.1%)
**失败数**: 53 (67.9%)

---

## 一、通过的API模块 (25个)

### 1. 认证模块 (4/5 通过)
- ✅ 投资人登录
- ✅ 融资方登录
- ✅ 投资人登录(重试)
- ✅ 融资方登录(重试)

### 2. 用户资料模块 (4/8 通过)
- ✅ 获取用户资料
- ✅ 更新用户资料
- ✅ 获取隐私设置

### 3. 投资人Dashboard模块 (4/4 全部通过)
- ✅ 投资人Dashboard
- ✅ 获取统计数据
- ✅ 获取推荐项目
- ✅ 获取最近活动

### 4. 投资人收藏模块 (2/6 通过)
- ✅ 获取收藏列表
- ✅ 获取收藏分组列表

### 5. 融资方Dashboard模块 (1/1 通过)
- ✅ 融资方Dashboard

### 6. 项目管理模块 (1/5 通过)
- ✅ 创建项目

### 7. 问答模块 (2/6 通过)
- ✅ 获取问答列表
- ✅ 获取收到的问题列表

### 8. 通知模块 (4/6 通过)
- ✅ 获取通知列表
- ✅ 获取未读通知
- ✅ 获取未读数量
- ✅ 全部标记已读

---

## 二、失败原因分类

### A类：数据库结构问题 (需要修复数据库)

| 错误码 | 说明 | 影响API数 |
|-------|------|----------|
| 500 | applications表可能仍缺少teaser_id列 | 6个 |
| 500 | investment_analyses表结构问题 | 5个 |

**修复建议**: 重启应用服务以执行DatabaseSyncRunner同步

### B类：测试数据缺失 (业务逻辑正常，缺少测试数据)

| 错误码 | 说明 | 影响API数 |
|-------|------|----------|
| 40004 | Teaser不存在 | 7个 |
| 40001 | 项目不存在 | 1个 |
| 40022 | 用户资料不存在(投资人/融资方资料未创建) | 4个 |
| 60001 | 问答记录不存在 | 4个 |
| 60006 | 通知不存在 | 2个 |
| 40016 | BP不存在 | 2个 |
| 40021 | 收藏记录不存在 | 1个 |

**修复建议**: 创建完整的测试数据

### C类：业务逻辑/参数验证问题

| 错误码 | 说明 | 影响API数 |
|-------|------|----------|
| 400 | 参数验证失败 | 1个 |
| 20005 | 刷新Token失败(refreshToken无效) | 1个 |

### D类：权限问题 (功能正常，需要管理员账号)

| 错误码 | 说明 | 影响API数 |
|-------|------|----------|
| 403 | 无管理员权限 | 6个 |

**说明**: 这是正常的安全行为，使用管理员账号测试即可通过

### E类：服务层实现问题 (需要检查代码)

| 端点 | 错误 | 可能原因 |
|------|------|---------|
| POST /investor/qa | 500 | 服务层异常 |
| POST /entrepreneur/teasers | 500 | 服务层异常 |
| POST /entrepreneur/teasers/auto-generate/1 | 500 | 服务层异常 |
| GET/PUT /entrepreneur/projects/* | 500 | 服务层异常 |
| GET /entrepreneur/bp/project/1 | 500 | 服务层异常 |
| POST /common/upload/* | 500 | 需要multipart/form-data |

---

## 三、优先修复建议

### 1. 高优先级 (核心功能)
1. **重启应用服务** - 确保DatabaseSyncRunner执行同步applications表
2. **创建测试数据** - 添加投资人资料、融资方资料、Teaser等基础数据
3. **修复InvestmentAnalysisService** - 检查500错误的根本原因

### 2. 中优先级 (功能完善)
1. 检查并修复ProjectService的查询方法
2. 检查EntrepreneurTeaserService的创建方法
3. 修复QaService的发送问题方法

### 3. 低优先级 (可延后)
1. 文件上传功能(需要multipart处理)
2. 管理员功能测试(需要管理员账号)
3. Token刷新功能

---

## 四、API文档

**Swagger UI**: http://localhost:8080/swagger-ui.html
**OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## 五、下一步行动

1. 重启应用服务以应用数据库同步
2. 运行以下SQL创建测试数据:

```sql
-- 确保用户资料存在
INSERT INTO investor_profiles (user_id, created_at, updated_at)
SELECT id, NOW(), NOW() FROM users WHERE user_type = 'INVESTOR'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO entrepreneur_profiles (user_id, created_at, updated_at)
SELECT id, NOW(), NOW() FROM users WHERE user_type = 'ENTREPRENEUR'
ON DUPLICATE KEY UPDATE updated_at = NOW();
```

3. 再次运行测试验证修复效果
