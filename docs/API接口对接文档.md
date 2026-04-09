# 投融资对接平台 - 前端接口对接文档

> **Base URL**: `http://{host}:8080/api/v1`
> **版本**: v1.0
> **更新日期**: 2026-04-09

---

## 目录

- [1. 通用约定](#1-通用约定)
  - [1.1 统一响应格式](#11-统一响应格式)
  - [1.2 认证方式](#12-认证方式)
  - [1.3 分页格式](#13-分页格式)
  - [1.4 错误码一览](#14-错误码一览)
  - [1.5 枚举值说明](#15-枚举值说明)
- [2. 认证模块](#2-认证模块)
- [3. 用户资料模块](#3-用户资料模块)
- [4. 投资人工作台](#4-投资人工作台)
- [5. Teaser浏览与搜索（投资人端）](#5-teaser浏览与搜索投资人端)
- [6. 投资人收藏](#6-投资人收藏)
- [7. 投资分析](#7-投资分析)
- [8. 投资人问答](#8-投资人问答)
- [9. 投资人申请](#9-投资人申请)
- [10. 融资方Dashboard](#10-融资方dashboard)
- [11. 融资方项目管理](#11-融资方项目管理)
- [12. 融资方Teaser管理](#12-融资方teaser管理)
- [13. 融资方商业计划书](#13-融资方商业计划书)
- [14. 融资方问答](#14-融资方问答)
- [15. 融资方申请管理](#15-融资方申请管理)
- [16. 通知模块](#16-通知模块)
- [17. 文件上传](#17-文件上传)
- [18. 后台管理](#18-后台管理)

---

## 1. 通用约定

### 1.1 统一响应格式

所有接口返回 JSON，统一包装在以下结构中：

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2026-04-09 10:30:00"
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | Integer | 业务状态码，`200` 表示成功 |
| `message` | String | 响应消息 |
| `data` | Object/null | 业务数据，`null` 字段不会返回 |
| `timestamp` | String | 服务器时间 |

> 前端判断逻辑：`code === 200` 为成功。

### 1.2 认证方式

除登录/注册外，所有接口需在请求头中携带 Token：

```
Authorization: Bearer <token>
```

Token 通过登录接口获取，有效期 **2小时**。过期后使用 refreshToken 刷新。

### 1.3 分页格式

分页接口返回 Spring Data 标准分页结构：

```json
{
  "code": 200,
  "data": {
    "content": [ ... ],        // 数据列表
    "totalElements": 100,      // 总记录数
    "totalPages": 5,           // 总页数
    "number": 0,               // 当前页（从0开始）
    "size": 20,                // 每页大小
    "first": true,             // 是否首页
    "last": false,             // 是否末页
    "numberOfElements": 20     // 当前页实际条数
  }
}
```

> 前端传参：`page` 从 **1** 开始（后端自动转换为从0开始）。

### 1.4 错误码一览

| 错误码 | 说明 | 前端处理建议 |
|--------|------|-------------|
| 200 | 成功 | - |
| 400 | 请求参数错误 | 检查表单 |
| 401 | 未登录 | 跳转登录页 |
| 403 | 无权限 | 提示无权限 |
| **20002** | Token已过期 | 用refreshToken刷新 |
| **20003** | Token无效 | 跳转登录页 |
| **20005** | Refresh Token无效 | 跳转登录页 |
| **20006** | 账号已禁用 | 提示联系客服 |
| **20009** | 密码错误 | 提示重试 |
| **30001** | 用户不存在 | 提示注册 |
| **30003** | 手机号已注册 | 引导登录 |
| **30004** | 验证码错误 | 提示重输 |
| **30009** | 认证审核中 | 提示等待审核 |
| **40001** | 项目不存在 | 返回列表页 |
| **40004** | Teaser不存在 | 返回列表页 |
| **40012** | Teaser已存在 | 提示用户 |
| **40016** | 商业计划书不存在 | 提示上传 |
| **40020** | 已收藏 | 勿重复操作 |
| **40021** | 收藏不存在 | 刷新页面 |
| **50001** | 申请不存在 | 返回列表页 |
| **50002** | 申请已审核 | 刷新页面 |
| **50005** | 申请已存在 | 勿重复提交 |
| **60001** | 问答不存在 | 返回列表页 |
| **70001** | 分析不存在 | 创建分析 |
| **70009** | 无权限操作 | 提示无权限 |
| **70010** | 认证记录不存在 | 刷新页面 |
| **80002** | 文件上传失败 | 重试 |
| **500** | 系统内部错误 | 联系后端 |

### 1.5 枚举值说明

#### 用户类型 `userType`

| 值 | 说明 |
|----|------|
| `INVESTOR` | 投资人 |
| `ENTREPRENEUR` | 融资方 |
| `ADMIN` | 管理员 |

#### 用户状态 `status`

| 值 | 说明 |
|----|------|
| `active` | 正常 |
| `pending` | 待激活 |
| `inactive` | 未激活 |
| `banned` | 已封禁 |

#### 行业类型 `industry`

| 值 | 说明 |
|----|------|
| `enterprise_service` | 企业服务 |
| `ai` | 人工智能 |
| `fintech` | 金融科技 |
| `healthcare` | 医疗健康 |
| `consumer` | 消费零售 |
| `new_energy` | 新能源 |
| `education` | 教育培训 |
| `entertainment` | 游戏娱乐 |
| `manufacturing` | 制造业 |
| `other` | 其他 |

#### 融资阶段 `financingStage`

| 值 | 说明 |
|----|------|
| `seed` | 种子轮 |
| `angel` | 天使轮 |
| `pre_a` | Pre-A轮 |
| `a` | A轮 |
| `b` | B轮 |
| `c` | C轮 |
| `d` | D轮 |
| `pre_ipo` | Pre-IPO |
| `ipo` | IPO上市 |

#### 项目状态 `projectStatus`

| 值 | 说明 |
|----|------|
| `draft` | 草稿 |
| `published` | 已发布 |
| `hidden` | 已隐藏 |
| `archived` | 已归档 |

#### Teaser状态 `teaserStatus`

| 值 | 说明 |
|----|------|
| `draft` | 草稿 |
| `published` | 已发布 |
| `hidden` | 已下架 |

#### 申请类型 `applicationType`

| 值 | 说明 |
|----|------|
| `get_bp` | 获取BP |
| `contact_company` | 联系企业 |
| `view_contact` | 查看联系方式 |

#### 申请状态 `applicationStatus`

| 值 | 说明 |
|----|------|
| `pending` | 待审核 |
| `approved` | 已批准 |
| `rejected` | 已拒绝 |
| `cancelled` | 已取消 |

#### 问答状态 `questionStatus`

| 值 | 说明 |
|----|------|
| `pending` | 待回答 |
| `answered` | 已回答 |
| `ignored` | 已忽略 |

#### 通知类型 `notificationType`

| 值 | 说明 |
|----|------|
| `system` | 系统通知 |
| `new_question` | 新问题 |
| `question_received` | 收到问题 |
| `question_answered` | 问题已回答 |
| `new_application` | 新申请 |
| `application_reviewed` | 申请已审核 |
| `bp_request_approved` | BP申请已批准 |
| `bp_request_rejected` | BP申请已拒绝 |
| `contact_request_approved` | 联系方式申请已批准 |
| `contact_request_rejected` | 联系方式申请已拒绝 |
| `verification_approved` | 实名认证已通过 |
| `verification_rejected` | 实名认证已拒绝 |
| `new_project` | 新项目 |
| `new_teaser` | 新Teaser |
| `ai_analysis_ready` | AI分析完成 |
| `system_announcement` | 系统公告 |

---

## 2. 认证模块

### 2.1 用户登录

```
POST /auth/login
```

**无需认证**

**请求体：**

```json
{
  "phone": "13800138001",     // 必填，正则: ^1[3-9]\d{9}$
  "password": "Test1234",     // 必填
  "loginType": "PASSWORD"     // 可选，默认 PASSWORD。枚举: PASSWORD | SMS_CODE
}
```

**响应：**

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJ...",
    "refreshToken": "eyJhbGciOiJ...",
    "user": {
      "id": 1,
      "phone": "13800138001",
      "email": "investor@test.com",
      "userType": "INVESTOR",
      "realName": "张三",
      "avatarUrl": "https://...",
      "isVerified": true,
      "status": "active"
    }
  }
}
```

> 前端需存储 `token` 和 `refreshToken`，后续请求携带 `token`。

---

### 2.2 用户注册

```
POST /auth/register
```

**无需认证**

**请求体：**

```json
{
  "phone": "13800000001",        // 必填，11位手机号
  "code": "123456",              // 必填，6位验证码
  "password": "Test1234",        // 必填，8-20位，必须包含字母和数字
  "userType": "INVESTOR",        // 必填，INVESTOR | ENTREPRENEUR
  "agreedToTerms": true          // 可选
}
```

**响应：**

```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "token": "eyJhbGciOiJ...",
    "refreshToken": "eyJhbGciOiJ...",
    "userType": "INVESTOR",
    "expiresAt": 1712640000000
  }
}
```

---

### 2.3 刷新Token

```
POST /auth/refresh
```

**无需认证**（但需要有效的 refreshToken）

**请求体：**

```json
{
  "refreshToken": "eyJhbGciOiJ..."   // 必填
}
```

**响应：**

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJ...",        // 新的access token
    "expiresAt": 1712640000000         // 新token过期时间（毫秒时间戳）
  }
}
```

---

### 2.4 修改密码

```
POST /auth/password/change
```

**需要认证**

**请求体：**

```json
{
  "oldPassword": "OldPass123",    // 必填
  "newPassword": "NewPass456"     // 必填，8-20位，须含字母和数字
}
```

**响应：**

```json
{ "code": 200, "message": "success", "data": null }
```

---

### 2.5 登出

```
POST /auth/logout
```

**需要认证**

无请求体。Token 失效。

**响应：**

```json
{ "code": 200, "message": "success", "data": null }
```

---

## 3. 用户资料模块

### 3.1 获取用户资料

```
GET /profile
```

**响应 `data`：**

```json
{
  "id": 1,
  "phone": "13800138001",
  "nickname": "投资人A",
  "email": "a@example.com",
  "avatarUrl": "https://...",
  "userType": "INVESTOR",
  "status": "active",
  "isVerified": true,
  "bio": "专注于AI赛道",
  "lastLoginAt": "2026-04-09 10:00:00",
  "createdAt": "2026-01-01 00:00:00"
}
```

---

### 3.2 更新用户资料

```
PUT /profile
```

**请求体（所有字段可选）：**

```json
{
  "nickname": "新昵称",          // 最多50字
  "email": "new@example.com",   // 合法邮箱
  "avatarUrl": "https://...",   // 头像URL
  "bio": "个人简介"              // 最多500字
}
```

---

### 3.3 获取投资人资料

```
GET /profile/investor
```

**响应 `data`：**

```json
{
  "id": 1,
  "institutionName": "XX资本",
  "position": "投资总监",
  "interestedIndustries": ["ai", "fintech"],
  "interestedStages": ["a", "b"],
  "minInvestmentAmount": 100,          // 单位：万元
  "maxInvestmentAmount": 5000,
  "interestedRegions": ["北京", "上海"],
  "investmentPhilosophy": "...",
  "investmentCases": "...",
  "verificationStatus": "approved",    // pending | approved | rejected
  "completeness": 85,                  // 资料完善度 0-100
  "updatedAt": "2026-04-01 10:00:00"
}
```

---

### 3.4 更新投资人资料

```
PUT /profile/investor
```

**请求体（所有字段可选）：**

```json
{
  "institutionName": "XX资本",           // 最多100字
  "position": "投资总监",                // 最多50字
  "interestedIndustries": ["ai"],       // 行业枚举值数组
  "interestedStages": ["a", "b"],       // 融资阶段枚举值数组
  "minInvestmentAmount": 100,            // ≥0，单位万元
  "maxInvestmentAmount": 5000,           // ≤1000000，单位万元
  "interestedRegions": ["北京"],
  "investmentPhilosophy": "...",         // 最多2000字
  "investmentCases": "..."               // 最多5000字
}
```

---

### 3.5 获取融资方资料

```
GET /profile/entrepreneur
```

**响应 `data`：**

```json
{
  "id": 1,
  "companyName": "XX科技",
  "industry": "ai",
  "financingStage": "a",
  "location": "北京",
  "companySize": "50-100人",
  "companyIntroduction": "...",
  "coreBusiness": "...",
  "targetFinancingAmount": 1000,        // 单位：万元
  "website": "https://...",
  "contactPhone": "13800138000",
  "contactEmail": "info@xx.com",
  "verificationStatus": "approved",
  "completeness": 90,
  "updatedAt": "2026-04-01 10:00:00"
}
```

---

### 3.6 更新融资方资料

```
PUT /profile/entrepreneur
```

**请求体（所有字段可选）：**

```json
{
  "companyName": "XX科技",               // 最多100字
  "industry": "ai",                      // 行业枚举值
  "financingStage": "a",                 // 融资阶段枚举值
  "location": "北京",                    // 最多100字
  "companySize": "50-100人",
  "companyIntroduction": "...",          // 最多2000字
  "coreBusiness": "...",                 // 最多1000字
  "targetFinancingAmount": 1000,         // 0~1000000万元
  "website": "https://...",              // 最多200字
  "contactPhone": "13800138000",         // 最多20字
  "contactEmail": "info@xx.com"          // 最多100字
}
```

---

### 3.7 提交实名认证

```
POST /profile/verification
```

**请求体：**

```json
{
  "realName": "张三",                          // 必填，最多50字
  "idCardNumber": "110101199001011234",        // 必填，18位身份证号
  "idCardFrontUrl": "https://.../front.jpg",   // 必填，身份证正面照URL
  "idCardBackUrl": "https://.../back.jpg",     // 必填，身份证反面照URL
  "verificationType": "investor",              // 可选，investor | entrepreneur
  "businessLicenseUrl": "https://...",          // 企业认证时需要
  "companyName": "XX科技"                       // 企业认证时需要
}
```

**响应 `data`：** 认证记录ID（数字）

> 已提交过会返回 `code: 30009`，前端提示"认证审核中"。

---

### 3.8 获取认证状态

```
GET /profile/verification/status
```

**响应 `data`：** 状态字符串（`pending` / `approved` / `rejected` / `null`）

---

### 3.9 获取隐私设置

```
GET /profile/privacy
```

**响应 `data`：**

```json
{
  "id": 1,
  "allowShowCompanyName": false,
  "allowShowFoundedTime": true,
  "allowShowCompanyScale": true,
  "allowShowOfficeAddress": false,
  "allowShowContactInfo": false,
  "allowShowFinancialData": false,
  "allowShowFinancingHistory": true,
  "allowShowFounderDetails": false,
  "allowShowTeamInfo": true,
  "allowPublicQa": true,
  "defaultQaPublic": true,
  "questionLibraryQaPublic": true,
  "requireBpApproval": true,
  "requireContactApproval": true,
  "allowViewQaRecords": true,
  "allowReceiveQuestions": true,
  "autoReplyTemplate": "感谢您的提问...",
  "createdAt": "2026-04-01 00:00:00",
  "updatedAt": "2026-04-09 10:00:00"
}
```

---

### 3.10 更新隐私设置

```
PUT /profile/privacy
```

**请求体（所有字段可选，Boolean类型）：**

```json
{
  "allowShowCompanyName": true,
  "allowShowContactInfo": false,
  "requireBpApproval": true,
  "requireContactApproval": true,
  "allowPublicQa": true,
  "autoReplyTemplate": "感谢提问..."
}
```

---

## 4. 投资人工作台

### 4.1 获取Dashboard

```
GET /investor/dashboard
```

**响应 `data`：**

```json
{
  "stats": {
    "viewedCount": 15,
    "favoriteCount": 8,
    "analyzedCount": 3,
    "pendingCount": 2
  },
  "recommendedTeasers": [ /* RecommendedTeaserResponse 数组，最多10条 */ ],
  "recentActivities": [ /* RecentActivityResponse 数组，最多5条 */ ]
}
```

---

### 4.2 获取统计数据

```
GET /investor/dashboard/stats
```

**响应 `data`：** DashboardStatsResponse（同上 stats 部分）

---

### 4.3 获取推荐项目

```
GET /investor/dashboard/recommended?limit=10
```

**参数：**

| 参数 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `limit` | Integer | 10 | 返回数量 |

**响应 `data`：** RecommendedTeaserResponse 数组

```json
[
  {
    "id": 1,
    "title": "AI智能分析平台",
    "summary": "企业级AI解决方案",
    "iconEmoji": "🤖",
    "industry": "ai",
    "financingStage": "a",
    "matchScore": 92,
    "viewCount": 156,
    "favoriteCount": 23,
    "isFavorite": false,
    "tags": ["AI", "B2B"],
    "createdAt": "2026-04-01 10:00:00"
  }
]
```

---

### 4.4 获取最近活动

```
GET /investor/dashboard/activities?limit=10
```

**响应 `data`：** RecentActivityResponse 数组

```json
[
  {
    "type": "view",
    "teaserId": 1,
    "teaserTitle": "AI智能分析平台",
    "description": "浏览了项目",
    "timestamp": "2026-04-09 10:00:00"
  }
]
```

---

## 5. Teaser浏览与搜索（投资人端）

### 5.1 获取Teaser列表

```
GET /investor/teasers?page=1&size=20
```

**响应：** 分页的 TeaserResponse

---

### 5.2 搜索Teaser

```
POST /investor/teasers/search
```

**请求体（所有字段可选）：**

```json
{
  "keyword": "AI",
  "industries": ["ai", "fintech"],
  "financingStages": ["a", "b"],
  "minFinancingAmount": 100,          // 万元
  "maxFinancingAmount": 5000,
  "region": "北京",
  "sortBy": "created_at",             // created_at | view_count | match_score
  "sortOrder": "desc",                // asc | desc
  "page": 1,
  "pageSize": 20
}
```

**响应：** 分页的 TeaserResponse

---

### 5.3 获取Teaser详情

```
GET /investor/teasers/{teaserId}
```

**响应 `data`：TeaserResponse**

```json
{
  "id": 1,
  "projectId": 1,
  "projectName": "AI智能分析平台",
  "title": "AI智能分析平台",
  "summary": "企业级AI解决方案提供商",
  "iconEmoji": "🤖",
  "industry": "ai",
  "financingStage": "a",
  "financingAmount": 1000,
  "financingPurpose": "产品研发",
  "location": "北京",
  "highlights": ["技术领先", "客户增长快"],
  "businessModel": "B2B SaaS",
  "targetMarket": "企业级市场",
  "competitiveAdvantage": "核心算法壁垒",
  "teamIntroduction": "核心团队来自BAT",
  "status": "published",
  "viewCount": 156,
  "favoriteCount": 23,
  "createdAt": "2026-04-01 10:00:00",
  "publishedAt": "2026-04-02 08:00:00"
}
```

---

### 5.4 获取推荐Teaser

```
GET /investor/teasers/recommended
```

**响应：** TeaserResponse 数组

---

### 5.5 获取热门Teaser

```
GET /investor/teasers/hot
```

**响应：** TeaserResponse 数组

---

## 6. 投资人收藏

### 6.1 获取收藏列表

```
GET /investor/favorites?page=1&size=20
```

**响应：** 分页的 FavoriteResponse

```json
{
  "content": [
    {
      "id": 1,
      "teaserId": 5,
      "teaserTitle": "AI平台",
      "teaserSummary": "企业级AI方案",
      "iconEmoji": "🤖",
      "industry": "ai",
      "financingStage": "a",
      "financingAmount": 1000,
      "groupName": "重点关注",
      "note": "下次跟进",
      "tags": ["AI"],
      "createdAt": "2026-04-09 10:00:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 6.2 添加收藏

```
POST /investor/favorites
```

**请求体：**

```json
{
  "teaserId": 5,                  // 必填
  "groupName": "默认分组",         // 可选
  "note": "感兴趣，需跟进"         // 可选
}
```

**HTTP状态码：201** | **响应 `data`：** FavoriteResponse

---

### 6.3 取消收藏

```
DELETE /investor/favorites/{teaserId}
```

**响应：** `{ "code": 200, "data": null }`

---

### 6.4 检查是否已收藏

```
GET /investor/favorites/check/{teaserId}
```

**响应 `data`：** `true` 或 `false`

---

### 6.5 更新收藏分组

```
PUT /investor/favorites/{teaserId}/group
```

**请求体：**

```json
{
  "groupName": "重要项目"
}
```

---

### 6.6 获取收藏分组列表

```
GET /investor/favorites/groups
```

**响应 `data`：** 字符串数组，如 `["默认分组", "重要项目", "AI赛道"]`

---

## 7. 投资分析

### 7.1 获取分析列表

```
GET /investor/analysis?page=1&size=20
```

**响应：** 分页的分析记录列表

---

### 7.2 创建投资分析

```
POST /investor/analysis
```

**请求体：**

```json
{
  "teaserId": 5,                      // 必填
  "analysisType": "comprehensive",     // 可选，默认 comprehensive
  "deepAnalysis": false,               // 可选，是否深度分析
  "customDimensions": ""               // 可选，自定义分析维度
}
```

**HTTP状态码：201**

---

### 7.3 获取分析详情

```
GET /investor/analysis/{analysisId}
```

---

### 7.4 获取某个Teaser的分析

```
GET /investor/analysis/teaser/{teaserId}
```

---

### 7.5 重新分析

```
POST /investor/analysis/{analysisId}/reanalyze
```

---

### 7.6 删除分析

```
DELETE /investor/analysis/{analysisId}
```

---

## 8. 投资人问答

### 8.1 获取问答列表

```
GET /investor/qa?page=1&size=20
```

**响应：** 分页的 QaRecordResponse

---

### 8.2 发送问题

```
POST /investor/qa
```

**请求体：**

```json
{
  "teaserId": 5,                          // 必填
  "question": "贵公司的核心竞争力是什么？",  // 必填，最多2000字
  "isPublic": true,                        // 可选，默认 true
  "category": "business_model"             // 可选
}
```

**HTTP状态码：201** | **响应 `data`：** QaRecordResponse

```json
{
  "id": 1,
  "teaserId": 5,
  "teaserTitle": "AI平台",
  "projectName": "AI智能分析平台",
  "questionerId": 22,
  "questionerName": "张三",
  "question": "贵公司的核心竞争力是什么？",
  "category": "business_model",
  "answer": null,
  "answererId": null,
  "answererName": null,
  "status": "pending",
  "isPublic": true,
  "questionedAt": "2026-04-09 10:30:00",
  "answeredAt": null,
  "createdAt": "2026-04-09 10:30:00"
}
```

---

### 8.3 获取问题详情

```
GET /investor/qa/{qaId}
```

**响应 `data`：** QaRecordResponse

---

## 9. 投资人申请

### 9.1 获取申请列表

```
GET /investor/applications?status=pending&page=1&size=20
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `status` | String | 可选，按状态筛选 |
| `page` | Integer | 页码，默认1 |
| `size` | Integer | 每页数量，默认20 |

---

### 9.2 创建申请

```
POST /investor/applications
```

**请求体：**

```json
{
  "teaserId": 5,                          // 必填
  "applicationType": "get_bp",            // 必填，枚举: get_bp | contact_company | view_contact
  "reason": "希望了解项目详情",             // 可选，最多1000字
  "institutionName": "XX资本",             // 联系企业时建议填写，最多100字
  "position": "投资经理"                    // 可选，最多50字
}
```

**HTTP状态码：201** | **响应 `data`：** ApplicationResponse

```json
{
  "id": 1,
  "teaserId": 5,
  "teaserTitle": "AI平台",
  "projectName": "AI智能分析平台",
  "applicantId": 22,
  "applicantName": "张三",
  "applicationType": "get_bp",
  "reason": "希望了解项目详情",
  "institutionName": "XX资本",
  "position": "投资经理",
  "status": "pending",
  "reviewComment": null,
  "reviewerId": null,
  "reviewedAt": null,
  "createdAt": "2026-04-09 10:30:00"
}
```

---

### 9.3 获取申请详情

```
GET /investor/applications/{applicationId}
```

---

## 10. 融资方Dashboard

### 10.1 获取Dashboard

```
GET /entrepreneur/dashboard
```

**响应 `data`：** 与投资人Dashboard结构类似，包含统计、推荐、活动信息。

---

## 11. 融资方项目管理

### 11.1 获取项目列表

```
GET /entrepreneur/projects?page=1&size=10
```

**响应：** 分页的 ProjectResponse

---

### 11.2 创建项目

```
POST /entrepreneur/projects
```

**请求体：**

```json
{
  "name": "AI智能分析平台",                // 必填，最多100字
  "summary": "企业级AI解决方案",           // 必填，最多200字
  "industry": "ai",                       // 必填，行业枚举值
  "financingStage": "a",                  // 必填，融资阶段枚举值
  "financingAmount": 1000,                // 可选，0~1000000万元
  "financingPurpose": "产品研发",          // 可选，最多500字
  "location": "北京",                     // 可选，最多100字
  "businessDescription": "...",            // 可选，最多5000字
  "businessModel": "B2B SaaS",            // 可选，最多2000字
  "targetMarket": "企业市场",              // 可选，最多1000字
  "competitiveAdvantage": "..."            // 可选，最多2000字
}
```

**HTTP状态码：201** | **响应 `data`：** ProjectResponse

```json
{
  "id": 1,
  "name": "AI智能分析平台",
  "summary": "企业级AI解决方案",
  "industry": "ai",
  "financingStage": "a",
  "financingAmount": 1000,
  "financingPurpose": "产品研发",
  "location": "北京",
  "businessDescription": "...",
  "businessModel": "B2B SaaS",
  "targetMarket": "企业市场",
  "competitiveAdvantage": "...",
  "status": "draft",
  "hasBp": false,
  "hasTeaser": false,
  "createdAt": "2026-04-09 10:00:00",
  "updatedAt": "2026-04-09 10:00:00"
}
```

---

### 11.3 获取项目详情

```
GET /entrepreneur/projects/{projectId}
```

---

### 11.4 更新项目

```
PUT /entrepreneur/projects/{projectId}
```

**请求体：** 同创建项目

---

### 11.5 删除项目

```
DELETE /entrepreneur/projects/{projectId}
```

---

## 12. 融资方Teaser管理

### 12.1 获取Teaser列表

```
GET /entrepreneur/teasers?page=1&size=10
```

---

### 12.2 创建Teaser

```
POST /entrepreneur/teasers
```

**请求体：**

```json
{
  "projectId": 1,                          // 必填
  "title": "AI智能分析平台",                // 必填，最多100字
  "summary": "企业级AI解决方案提供商",       // 必填，最多200字
  "iconEmoji": "🤖",                       // 可选
  "highlights": ["技术领先", "增长迅速"],    // 可选，字符串数组
  "businessModel": "B2B SaaS",             // 可选，最多2000字
  "targetMarket": "企业级市场",             // 可选，最多1000字
  "competitiveAdvantage": "核心算法壁垒",    // 可选，最多2000字
  "teamIntroduction": "核心团队来自BAT",     // 可选，最多2000字
  "autoGenerate": false                     // 可选，默认 false
}
```

**HTTP状态码：201** | **响应 `data`：** TeaserResponse（状态为 `draft`）

> 注意：一个项目只能创建一个Teaser，重复创建返回 `code: 40012`。

---

### 12.3 获取Teaser详情

```
GET /entrepreneur/teasers/{teaserId}
```

---

### 12.4 更新Teaser

```
PUT /entrepreneur/teasers/{teaserId}
```

**请求体：** 同创建Teaser

> 只有 `draft` 状态的Teaser可以编辑。已发布的返回 `code: 40013`。

---

### 12.5 发布Teaser

```
POST /entrepreneur/teasers/{teaserId}/publish
```

无请求体。发布后投资人可看到此Teaser。

---

### 12.6 下架Teaser

```
POST /entrepreneur/teasers/{teaserId}/unpublish
```

无请求体。

---

### 12.7 自动生成Teaser

```
POST /entrepreneur/teasers/auto-generate/{projectId}
```

无请求体。根据项目的BP自动生成Teaser。

> 项目已有Teaser时返回 `code: 40012`。

---

### 12.8 删除Teaser

```
DELETE /entrepreneur/teasers/{teaserId}
```

---

## 13. 融资方商业计划书

### 13.1 获取项目的BP列表

```
GET /entrepreneur/bp/project/{projectId}
```

---

### 13.2 上传BP

```
POST /entrepreneur/bp/upload/{projectId}
```

**Content-Type**: `multipart/form-data`

**表单参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `file` | File | BP文件，支持 pdf/doc/docx/ppt/pptx |

---

### 13.3 获取BP详情

```
GET /entrepreneur/bp/{bpId}
```

---

### 13.4 删除BP

```
DELETE /entrepreneur/bp/{bpId}
```

---

## 14. 融资方问答

### 14.1 获取收到的问题列表

```
GET /entrepreneur/qa?status=pending&page=1&size=20
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `status` | String | 可选，按状态筛选 |
| `page` | Integer | 页码 |
| `size` | Integer | 每页数量 |

---

### 14.2 获取问题详情

```
GET /entrepreneur/qa/{qaId}
```

---

### 14.3 回答问题

```
POST /entrepreneur/qa/{qaId}/answer
```

**请求体：**

```json
{
  "answer": "我们的核心竞争力是...",      // 必填，最多5000字
  "isPublic": true                        // 可选，默认 true
}
```

---

## 15. 融资方申请管理

### 15.1 获取收到的申请列表

```
GET /entrepreneur/applications?status=pending&page=1&size=20
```

---

### 15.2 获取申请详情

```
GET /entrepreneur/applications/{applicationId}
```

---

### 15.3 审核申请

```
POST /entrepreneur/applications/{applicationId}/review?approved=true&comment=审核通过
```

> **注意：** 参数通过 **URL查询参数** 传递，不是JSON请求体！

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `approved` | Boolean | 是 | `true` 批准 / `false` 拒绝 |
| `comment` | String | 否 | 审核意见 |

---

## 16. 通知模块

### 16.1 获取通知列表

```
GET /notifications?page=1&size=20
```

**响应：** 分页的 NotificationResponse

```json
{
  "content": [
    {
      "id": 1,
      "type": "new_question",
      "title": "您收到一个新的问题",
      "content": "投资人对您的项目提出了问题",
      "relatedId": 5,
      "relatedType": "QA",
      "isRead": false,
      "createdAt": "2026-04-09 10:30:00"
    }
  ],
  "totalElements": 10,
  "totalPages": 1
}
```

---

### 16.2 获取未读通知

```
GET /notifications/unread
```

**响应 `data`：** NotificationResponse 数组（不分页）

---

### 16.3 获取未读数量

```
GET /notifications/unread/count
```

**响应 `data`：** 数字（如 `5`）

> 前端可用此接口实现角标提醒。

---

### 16.4 标记已读

```
PUT /notifications/{notificationId}/read
```

---

### 16.5 全部标记已读

```
PUT /notifications/read-all
```

---

### 16.6 删除通知

```
DELETE /notifications/{notificationId}
```

---

### 16.7 清空所有通知

```
DELETE /notifications/clear
```

---

## 17. 文件上传

所有上传接口使用 `multipart/form-data` 格式。

### 17.1 上传图片

```
POST /common/upload/image
```

| 表单参数 | 类型 | 说明 |
|----------|------|------|
| `file` | File | 图片文件，支持 jpg/jpeg/png/gif/bmp/webp |
| `dir` | String | 可选，子目录，默认 `common` |

**HTTP状态码：201** | **响应 `data`：**

```json
{
  "fileName": "photo.jpg",
  "filePath": "/uploads/dev/images/common/2026/04/09/uuid.jpg",
  "fileUrl": "/uploads/dev/images/common/2026/04/09/uuid.jpg",
  "fileSize": 102400,
  "fileType": "jpg"
}
```

---

### 17.2 上传文档

```
POST /common/upload/document
```

| 表单参数 | 类型 | 说明 |
|----------|------|------|
| `file` | File | 文档文件，支持 pdf/doc/docx/xls/xlsx/ppt/pptx |
| `dir` | String | 可选，子目录 |

---

### 17.3 上传头像

```
POST /common/upload/avatar
```

| 表单参数 | 类型 | 说明 |
|----------|------|------|
| `file` | File | 头像图片 |

---

## 18. 后台管理

> **需要管理员权限** (`ADMIN`)

### 18.1 管理员Dashboard

```
GET /admin/dashboard
```

**响应 `data`：**

```json
{
  "totalUsers": 100,
  "investorCount": 60,
  "entrepreneurCount": 35,
  "todayNewUsers": 5,
  "totalProjects": 40,
  "publishedTeaserCount": 25,
  "pendingVerificationCount": 3,
  "pendingApplicationCount": 8,
  "totalViewCount": 5000,
  "totalFavoriteCount": 200,
  "activeUserCount": 45
}
```

---

### 18.2 获取用户列表

```
GET /admin/users?userType=INVESTOR&status=active&keyword=张&page=1&size=20
```

| 参数 | 类型 | 说明 |
|------|------|------|
| `userType` | String | 可选，按用户类型筛选 |
| `status` | String | 可选，按状态筛选 |
| `keyword` | String | 可选，搜索昵称/手机号 |
| `page` | Integer | 页码 |
| `size` | Integer | 每页数量 |

**响应：** 分页的 UserListResponse

---

### 18.3 更新用户状态

```
PUT /admin/users/{userId}/status
```

**请求体：**

```json
{
  "status": "banned",           // 必填，active | pending | inactive | banned
  "reason": "违反社区规则"        // 可选
}
```

---

### 18.4 获取认证列表

```
GET /admin/verifications?status=pending&page=1&size=20
```

---

### 18.5 获取认证详情

```
GET /admin/verifications/{verificationId}
```

---

### 18.6 审核认证

```
POST /admin/verifications/{verificationId}/review
```

**请求体：**

```json
{
  "approved": true,               // 必填
  "comment": "审核通过"            // 可选，最多500字
}
```

---

## 附录：前端对接快速上手

### Axios 封装示例

```javascript
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 15000
})

// 请求拦截 - 自动添加Token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截 - 统一错误处理
api.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data  // 直接返回data，简化调用
    }
    return Promise.reject({ code, message })
  },
  error => {
    if (error.response?.status === 401) {
      // Token过期，尝试刷新
      refreshToken()
    }
    return Promise.reject(error)
  }
)

// 刷新Token
async function refreshToken() {
  const rt = localStorage.getItem('refreshToken')
  if (!rt) {
    window.location.href = '/login'
    return
  }
  try {
    const { data } = await axios.post('/api/v1/auth/refresh', { refreshToken: rt })
    localStorage.setItem('token', data.token)
    // 重试之前的请求
  } catch {
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    window.location.href = '/login'
  }
}
```

### 登录流程

```javascript
// 1. 登录
const { token, refreshToken, user } = await api.post('/auth/login', {
  phone: '13800138001',
  password: 'Test1234'
})

// 2. 存储凭证
localStorage.setItem('token', token)
localStorage.setItem('refreshToken', refreshToken)
localStorage.setItem('userInfo', JSON.stringify(user))

// 3. 根据角色跳转
if (user.userType === 'INVESTOR') {
  router.push('/investor/dashboard')
} else if (user.userType === 'ENTREPRENEUR') {
  router.push('/entrepreneur/dashboard')
}
```

### 分页组件对接

```javascript
// 后端 page 从1开始
const { content, totalElements, totalPages } = await api.get('/investor/teasers', {
  params: { page: currentPage, size: 20 }
})

// totalElements -> 总记录数
// totalPages -> 总页数
// content -> 当前页数据
```
