# Investment Platform Backend Service

## 项目简介

投融资对接平台后端服务，基于 Spring Boot 3.2.x + Java 17 + MySQL 8.0 构建。
为投资人和融资用户提供高效的对接平台，支持项目展示、商业计划书管理、在线问答、申请审核等功能。

## 技术栈

- **Java**: JDK 17
- **框架**: Spring Boot 3.2.x
- **构建工具**: Maven 3.6+
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA
- **认证**: Spring Security + JWT
- **文档**: SpringDoc OpenAPI (Swagger)
- **工具**: Lombok, MapStruct, Hutool
- **缓存**: Redis (可选)
- **文件存储**: 本地存储

## 项目结构

```
investment/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/investment/
│   │   ├── InvestmentApplication.java      # 主启动类
│   │   ├── common/                         # 通用模块
│   │   │   ├── config/                     # 配置类
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   ├── WebMvcConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── AsyncConfig.java
│   │   │   ├── constant/                  # 常量定义
│   │   │   ├── exception/                 # 异常处理
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── ErrorCode.java
│   │   │   └── response/                  # 响应封装
│   │   │       ├── ApiResponse.java
│   │   │       └── PageResponse.java
│   │   ├── entity/                         # 实体类 (20+)
│   │   │   ├── User.java
│   │   │   ├── InvestorProfile.java
│   │   │   ├── EntrepreneurProfile.java
│   │   │   ├── Project.java
│   │   │   ├── BusinessPlan.java
│   │   │   ├── Teaser.java
│   │   │   ├── Application.java
│   │   │   ├── QaRecord.java
│   │   │   └── ...
│   │   ├── enums/                          # 枚举类 (15+)
│   │   │   ├── UserType.java
│   │   │   ├── UserStatus.java
│   │   │   ├── IndustryType.java
│   │   │   ├── FinancingStage.java
│   │   │   └── ...
│   │   ├── repository/                     # 数据访问层 (20+)
│   │   │   ├── UserRepository.java
│   │   │   ├── ProjectRepository.java
│   │   │   ├── TeaserRepository.java
│   │   │   └── ...
│   │   ├── service/                        # 服务层 (15+)
│   │   │   ├── AuthService.java
│   │   │   ├── InvestorDashboardService.java
│   │   │   ├── EntrepreneurDashboardService.java
│   │   │   ├── ProjectService.java
│   │   │   ├── TeaserSearchService.java
│   │   │   ├── QaService.java
│   │   │   ├── ApplicationService.java
│   │   │   ├── AdminService.java
│   │   │   ├── UserProfileService.java
│   │   │   ├── NotificationService.java
│   │   │   └── ...
│   │   ├── controller/                     # 控制器层 (15+)
│   │   │   ├── AuthController.java
│   │   │   ├── InvestorDashboardController.java
│   │   │   ├── InvestorTeaserController.java
│   │   │   ├── EntrepreneurDashboardController.java
│   │   │   ├── EntrepreneurProjectController.java
│   │   │   ├── AdminController.java
│   │   │   └── ...
│   │   ├── dto/                            # 数据传输对象 (50+)
│   │   │   ├── request/
│   │   │   │   ├── auth/
│   │   │   │   ├── investor/
│   │   │   │   ├── entrepreneur/
│   │   │   │   ├── admin/
│   │   │   │   └── common/
│   │   │   └── response/
│   │   │       ├── auth/
│   │   │       ├── investor/
│   │   │       ├── entrepreneur/
│   │   │       ├── admin/
│   │   │       └── common/
│   │   ├── security/                       # 安全模块
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtTokenProvider.java
│   │   │   ├── UserPrincipal.java
│   │   │   └── CustomUserDetailsService.java
│   │   └── util/                            # 工具类
│   │       ├── JwtUtil.java
│   │       ├── PasswordUtil.java
│   │       ├── FileUtil.java
│   │       └── ValidationUtil.java
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── logback-spring.xml
└── uploads/                                  # 文件上传目录
```

## 数据库表

| 表名 | 描述 |
|------|------|
| users | 用户表 |
| investor_profiles | 投资人资料表 |
| entrepreneur_profiles | 融资用户资料表 |
| user_verifications | 实名认证表 |
| projects | 项目表 |
| business_plans | 商业计划书表 |
| teasers | Teaser表 |
| applications | 申请表 |
| qa_records | 问答记录表 |
| investor_questions | 投资人问题库表 |
| investment_analyses | 投资分析表 |
| notifications | 通知表 |
| privacy_settings | 隐私设置表 |
| roles | 角色表 |
| permissions | 权限表 |
| user_roles | 用户角色关联表 |
| role_permissions | 角色权限关联表 |
| operation_logs | 操作日志表 |
| statistics | 统计表 |
| system_configs | 系统配置表 |

## API模块

### 1. 认证模块 (/auth)
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /auth/register | 用户注册 |
| POST | /auth/login | 用户登录 |
| POST | /auth/logout | 用户登出 |
| POST | /auth/refresh | 刷新Token |
| POST | /auth/send-code | 发送验证码 |
| POST | /auth/reset-password | 重置密码 |

### 2. 投资人模块 (/investor)
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /investor/dashboard | 工作台首页 |
| GET | /investor/teasers/search | 搜索Teaser |
| GET | /investor/teasers/{id} | Teaser详情 |
| POST | /investor/favorites | 收藏项目 |
| DELETE | /investor/favorites/{teaserId} | 取消收藏 |
| GET | /investor/favorites | 收藏列表 |
| POST | /investor/analysis | 创建投资分析 |
| GET | /investor/analysis | 分析列表 |
| POST | /investor/qa | 发送问题 |
| GET | /investor/qa | 我的问答列表 |
| POST | /investor/applications | 提交申请 |
| GET | /investor/applications | 我的申请列表 |

### 3. 融资用户模块 (/entrepreneur)
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /entrepreneur/dashboard | 工作台首页 |
| POST | /entrepreneur/projects | 创建项目 |
| GET | /entrepreneur/projects | 项目列表 |
| PUT | /entrepreneur/projects/{id} | 更新项目 |
| DELETE | /entrepreneur/projects/{id} | 删除项目 |
| POST | /entrepreneur/bp/upload/{projectId} | 上传BP |
| GET | /entrepreneur/bp/project/{projectId} | 项目BP列表 |
| POST | /entrepreneur/teasers | 创建Teaser |
| GET | /entrepreneur/teasers | Teaser列表 |
| POST | /entrepreneur/teasers/{id}/publish | 发布Teaser |
| POST | /entrepreneur/teasers/{id}/unpublish | 下架Teaser |
| GET | /entrepreneur/qa | 收到的问题列表 |
| POST | /entrepreneur/qa/{qaId}/answer | 回答问题 |
| GET | /entrepreneur/applications | 收到的申请列表 |
| POST | /entrepreneur/applications/{id}/review | 审核申请 |

### 4. 后台管理模块 (/admin)
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /admin/dashboard | 管理首页统计 |
| GET | /admin/users | 用户列表 |
| PUT | /admin/users/{id}/status | 更新用户状态 |
| GET | /admin/verifications | 认证列表 |
| GET | /admin/verifications/{id} | 认证详情 |
| POST | /admin/verifications/{id}/review | 审核认证 |

### 5. 通用模块 (/common)
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /common/upload/image | 上传图片 |
| POST | /common/upload/document | 上传文档 |
| POST | /common/upload/avatar | 上传头像 |
| GET | /notifications | 通知列表 |
| GET | /notifications/unread | 未读通知 |
| PUT | /notifications/{id}/read | 标记已读 |
| PUT | /notifications/read-all | 全部标记已读 |

### 6. 用户资料模块 (/profile)
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /profile | 获取用户信息 |
| PUT | /profile | 更新用户信息 |
| GET | /profile/investor | 获取投资人资料 |
| PUT | /profile/investor | 更新投资人资料 |
| GET | /profile/entrepreneur | 获取融资用户资料 |
| PUT | /profile/entrepreneur | 更新融资用户资料 |
| POST | /profile/verification | 提交实名认证 |
| GET | /profile/verification/status | 认证状态 |
| GET | /profile/privacy | 获取隐私设置 |
| PUT | /profile/privacy | 更新隐私设置 |

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 数据库配置

1. 创建数据库
```sql
CREATE DATABASE investment CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `application-dev.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/investment
    username: your_username
    password: your_password
```

### 启动

```bash
# 编译项目
mvn clean install -DskipTests

# 启动服务
mvn spring-boot:run

# 或者打包后运行
java -jar target/investment-1.0.0.jar
```

### 访问Swagger文档

启动后访问: http://localhost:8080/api/v1/swagger-ui.html

## 项目特性

### 安全特性
- JWT Token认证
- 密码BCrypt加密
- 接口权限控制
- 参数验证
- SQL注入防护
- XSS攻击防护

### 业务特性
- 投资人工作台
- 融资用户工作台
- 项目管理
- Teaser自动生成
- 商业计划书上传
- 在线问答系统
- 申请审核流程
- 投资分析（AI Mock）
- 实名认证
- 隐私设置
- 消息通知
- 后台管理

### 技术特性
- RESTful API设计
- 统一响应封装
- 全局异常处理
- 分页查询封装
- 异步任务支持
- 操作日志记录
- 接口文档自动生成

## 代码规范

- 遵循阿里巴巴Java开发规范
- 使用Lombok简化代码
- 使用MapStruct进行对象映射
- 统一的命名规范
- 完善的注释文档

## 文件统计

| 类型 | 数量 |
|------|------|
| 实体类 | 20+ |
| 枚举类 | 15+ |
| Repository | 20+ |
| Service | 15+ |
| Controller | 15+ |
| DTO | 50+ |
| 配置类 | 10+ |
| 工具类 | 5+ |
| **总计** | **150+** |

## 许可证

Copyright © 2026 Investment Team
