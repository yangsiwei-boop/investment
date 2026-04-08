# Entity与数据库结构对比分析报告

## 问题概述

代码中的Entity定义与数据库schema.sql中的表结构存在多处不一致，导致运行时出现"Unknown column"错误。

---

## 1. investor_activities 表

**问题**: 数据库schema.sql中没有定义`investor_activities`表！

数据库中存在的是独立的表：
- `favorites` - 收藏表
- `view_histories` - 浏览记录表
- `investment_analyses` - 投资分析表

**代码中的InvestorActivity.java定义**:
```java
@Entity
@Table(name = "investor_activities")
public class InvestorActivity {
    private Long id;
    private Long investorUserId;      // 数据库可能有
    private Long teaserId;            // @Transient - 数据库没有
    private String activityType;      // 数据库可能有
    private String groupName;         // @Transient - 数据库没有
    private String notes;             // @Transient - 数据库没有
    private LocalDateTime updatedAt;  // @Transient - 数据库没有
}
```

**建议方案**:
- 方案A: 删除InvestorActivity，改用独立的Favorite、ViewHistory、InvestmentAnalysis实体
- 方案B: 在数据库中创建完整的investor_activities表

---

## 2. investment_analyses 表

**数据库定义** (schema.sql 第382-398行):
```sql
CREATE TABLE investment_analyses (
    id BIGINT PRIMARY KEY,
    teaser_id BIGINT NOT NULL,
    investor_user_id BIGINT NOT NULL,
    analysis_type VARCHAR(50),
    analysis_content JSON,
    score DECIMAL(5,2),
    recommendation VARCHAR(100),
    created_at, updated_at, deleted_at
);
```

**Entity定义** (InvestmentAnalysis.java):
- 有大量额外字段: overall_score, industry_analysis_score, team_analysis_score等
- 数据库中没有这些列

**建议**: 需要在数据库中添加缺失的列，或者简化Entity定义

---

## 3. favorites 表

**数据库定义** (schema.sql 第369-380行):
```sql
CREATE TABLE favorites (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    teaser_id BIGINT NOT NULL,
    created_at
);
```

**问题**: 代码中没有对应的Favorite实体，而是用InvestorActivity代替

---

## 4. view_histories 表

**数据库定义** (schema.sql 第400-414行):
```sql
CREATE TABLE view_histories (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    teaser_id BIGINT,
    project_id BIGINT,
    view_duration INT,
    created_at
);
```

**问题**: 代码中没有对应的ViewHistory实体，而是用InvestorActivity代替

---

## 5. entrepreneur_profiles 表

**数据库定义** (schema.sql 第76-109行):
包含 profile_completion_rate 列

**Entity定义**:
- profileCompletionRate 被标记为 @Transient

**建议**: 移除@Transient注解

---

## 6. notifications 表

**数据库定义** (schema.sql 第324-339行):
只有基本字段，没有 updated_at

**Entity定义**:
- updatedAt 被标记为 @Transient ✓ (正确)

---

## 7. qa_records 表

**数据库定义** (schema.sql 第260-289行):
只有基本字段

**Entity定义**:
- createdAt, updatedAt 被标记为 @Transient

---

## 8. applications 表

**数据库定义** (schema.sql 第228-257行):
包含 bp_id 列

**Entity定义**:
- bpId 被标记为 @Transient

---

## 解决方案建议

### 方案A: 修改代码适配数据库 (推荐)

1. 删除 InvestorActivity 实体
2. 创建 Favorite 实体对应 favorites 表
3. 创建 ViewHistory 实体对应 view_histories 表
4. 修改 InvestmentAnalysis 实体，只保留数据库中存在的字段
5. 移除各Entity中不必要的@Transient注解

### 方案B: 修改数据库适配代码

需要执行大量ALTER TABLE语句添加缺失的列，不推荐。

---

## 立即可执行的SQL修复

如果选择方案B，需要执行以下SQL:

```sql
-- 1. 如果investor_activities表存在但结构不完整
ALTER TABLE investor_activities ADD COLUMN IF NOT EXISTS teaser_id BIGINT;
ALTER TABLE investor_activities ADD COLUMN IF NOT EXISTS activity_type VARCHAR(20);
ALTER TABLE investor_activities ADD COLUMN IF NOT EXISTS group_name VARCHAR(100);
ALTER TABLE investor_activities ADD COLUMN IF NOT EXISTS notes TEXT;
ALTER TABLE investor_activities ADD COLUMN IF NOT EXISTS updated_at DATETIME;

-- 2. 为investment_analyses添加缺失字段
ALTER TABLE investment_analyses ADD COLUMN IF NOT EXISTS overall_score INT;
ALTER TABLE investment_analyses ADD COLUMN IF NOT EXISTS overall_verdict VARCHAR(20);
-- ... 更多字段
```

---

## 下一步行动

请确认选择哪个方案：
- **方案A**: 修改代码适配数据库结构
- **方案B**: 执行SQL脚本修改数据库结构
