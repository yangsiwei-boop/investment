# API测试报告 - 投融资平台

生成日期: 2026-04-08

## 测试环境
- 基础URL: http://localhost:8080/api/v1
- 测试账号
  - 投资人: 13800138001 /测试密码: Test1234
        - 融资方: 13800138003 /测试密码: Test1234

    - 用户类型: 投资人(INVESTor) / 融资方(entrepreneur)
    - 平台: http://localhost:8080
    - 平台管理员: http://localhost:8080/api/v1/entrepreneur/dashboard
    - 项目列表: http://localhost:8080/api/v1/entrepreneur/projects
    - Teaser列表: http://localhost:8080/api/v1/entrepreneur/teasers
    - 问答列表: http://localhost:8080/api/v1/entrepreneur/qa
        - 申请列表: http://localhost:8080/api/v1/entrepreneur/applications
    - 通知列表:http://localhost:8080/api/v1/notifications

- 登出: http://localhost:8080/api/v1/auth/logout
    - 刷新Token: http://localhost:8080/api/v1/auth/refresh
- 修改密码: http://localhost:8080/api/v1/auth/password/change
    - 登测试: http://localhost:8080/api/v1/auth/health
- 公共teaser列表: http://localhost:8080/api/v1/public/teasers
- Teaser搜索(分页): http://localhost:8080/api/v1/investor/teasers/search
- Teaser详情: http://localhost:8080/api/v1/investor/teasers/{teaserId}
- Teaser热门列表: http://localhost:8080/api/v1/investor/teasers/hot
- Teaser推荐列表: http://localhost:8080/api/v1/investor/teasers/recommended
- 收藏列表: http://localhost:8080/api/v1/investor/favorites
- 收藏分组列表: http://localhost:8080/api/v1/investor/favorites/groups
- 检查是否已收藏: http://localhost:8080/api/v1/investor/favorites/check/{teaserId}
- 投资分析列表: http://localhost:8080/api/v1/investor/analysis
- 投资分析详情: http://localhost:8080/api/v1/investor/analysis/{analysisId}
- 重新分析: http://localhost:8080/api/v1/investor/analysis/{analysisId}/reanalyze
- 删除分析: http://localhost:8080/api/v1/investor/analysis/{analysisId}
- 投资人问题列表: http://localhost:8080/api/v1/investor/qa
- 问答详情: http://localhost:8080/api/v1/investor/qa/{qaId}
- 发送问题: http://localhost:8080/api/v1/investor/qa
- 投资人申请列表: http://localhost:8080/api/v1/investor/applications
- 投资人申请详情: http://localhost:8080/api/v1/investor/applications/{applicationId}
- 融资方工作台: http://localhost:8080/api/v1/entrepreneur/dashboard
- 融资方项目列表: http://localhost:8080/api/v1/entrepreneur/projects
- 创建项目: http://localhost:8080/api/v1/entrepreneur/projects
- 获取项目详情: http://localhost:8080/api/v1/entrepreneur/projects/{projectId}
- 更新项目; http://localhost:8080/api/v1/entrepreneur/projects/{projectId}
- 删除项目; http://localhost:8080/api/v1/entrepreneur/projects/{projectId}
- Teaser列表: http://localhost:8080/api/v1/entrepreneur/teasers
- 创建Teaser; http://localhost:8080/api/v1/entrepreneur/teasers
- 自动生成Teaser; http://localhost:8080/api/v1/entrepreneur/teasers/auto-generate/{projectId}
- 获取Teaser详情; http://localhost:8080/api/v1/entrepreneur/teasers/{teaserId}
- 更新Teaser; http://localhost:8080/api/v1/entrepreneur/teasers/{teaserId}
- 发布Teaser; http://localhost:8080/api/v1/entrepreneur/teasers/{teaserId}/publish
    - 下架Teaser; http://localhost:8080/api/v1/entrepreneur/teasers/{teaserId}/unpublish
    - 删除Teaser; http://localhost:8080/api/v1/entrepreneur/teasers/{teaserId}
- 融资方问答列表: http://localhost:8080/api/v1/entrepreneur/qa
- 获取问答详情; http://localhost:8080/api/v1/entrepreneur/qa/{qaId}
- 回答问题; http://localhost:8080/api/v1/entrepreneur/qa/{qaId}/answer
- 融资方申请列表: http://localhost:8080/api/v1/entrepreneur/applications
- 获取申请详情; http://localhost:8080/api/v1/entrepreneur/applications/{applicationId}
- 审核申请; http://localhost:8080/api/v1/entrepreneur/applications/{applicationId}/review?approved=true&comment=Approved
- 通知列表: http://localhost:8080/api/v1/notifications
- 未读通知列表: http://localhost:8080/api/v1/notifications/unread
- 标记所有通知已读: http://localhost:8080/api/v1/notifications/read-all
    - 删除通知; http://localhost:8080/api/v1/notifications/{notificationId}
    - 清空所有通知: http://localhost:8080/api/v1/notifications/clear

- 登出: http://localhost:8080/api/v1/auth/logout
    - 刷新Token: http://localhost:8080/api/v1/auth/refresh
    - 修改密码: http://localhost:8080/api/v1/auth/password/change (400 Bad request)
    - 帱测试? http://localhost:8080/api/v1/auth/health (200 OK)
- 公共Teaser列表: http://localhost:8080/api/v1/public/teasers (200 OK)

    }

    ## 测试结果分析

    ## 发现的问题

    1. **数据问题**
        - 投资人Dashboard统计API返回空列表，可能是因为数据库中没有已发布的Teaser数据
        - 建议：在数据库中添加已发布的测试数据，或者初始化数据

    - **功能缺失**
        - `/investor/favorites/groups` 接口返回空列表，提示功能未实现
        - `/investor/teasers/search` 接口在搜索时没有考虑状态筛选，导致返回所有teaser，        - `Teasers/hot` 接口返回所有数据，应该是分页参数 `status` 为空
        - `/investor/teasers/{teaserId}/viewHistory` 接口不存在 (404)
        - Teaser详情、Teaser的`isFavorited` 字段检查失败，返回null而不是布尔值
        - Teaser推荐列表返回所有数据，应该分页参数 `status` 为空时
        - `/investor/teasers/recommended` 接口使用分页参数但没有默认值，推荐逻辑正确，返回空列表
        - `/investor/teasers/hot` 接口返回所有数据，应考虑热度排序
        - **数据库查询问题**
        - `TeaserSearchService` 的多个方法中存在SQL语法错误
        - 在Teaser搜索时，查询teaser详情时出现错误：`Specification` 字段验证失败且返回404
        - 数据库查询失败（`TeaserRepository` 未找到匹配的 Specification)
        - `viewHistoryRepository.save()` 方法中，`viewHistory` 实体引用了 `ViewHistory` 表，但该字段类型不匹配

        - **数据一致性**
        - 代码质量问题
            1. 收藏分组功能未实现：`InvestorFavoriteService.updateFavoriteGroup` 方法抛出异常，表示功能不可用，        - `InvestorFavoriteService.getFavoriteGroups` 方法返回空列表
        - 在获取收藏列表时， `teaser.isFavorited(teaserId)` 总是返回 false，而应该考虑优化数据库查询逻辑

        - 收藏列表查询中，如果收藏记录的teaser状态不是已发布，，应该返回空列表
    }
}
```java
// TeaserSearchService.java - 第119行
```java
            return teaserRepository.findById(teaserId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));
            }
        }
    }
}
```

问题是已识别并需要修复。以下是我的分析建议：

1. 在 `TeaserSearchService.java` 中修复SQL语法错误

检查 `teaserRepository.findById(teaserId)` 返回的 `Teaser` 对象。将其为 `TeaserSearchResponse` 后调用 `isFavorited(teaserId)` 方法，但该方法期望返回布尔值，        if (!isFavorited) {
                // 检查是否已收藏
                boolean isFavorited = favoriteRepository.existsByUserIdAndTeaserId(investorId, teaserId));
                return false;
            }
        }
    }
}
```

问题2: `teasers/hot` 接口SQL语法错误
```sql
SELECT t0.*, t1.* as t0, tc1, t2.*, t3.*, t4.*
    FROM teasers t
    where t.status = 'PUBLISHED'
    and t0.id not : (select * from teasers t where t.id not in (?)
    order by t.view_count desc
    limit ?;?) as t
    order by t.viewCount desc, ` + `";  OFFSET : limit;
    `Pageable pageable = PageRequest.of(page - 1, size, sort)
    if (request.getSortBy() != null && request.getSortOrder().isEmpty()) {
        sort.add(cb.like(root.get("status"), TeaserStatus.PUBLISHED));
            }
        }
    }
}
```
问题是已识别并需要修复。以下是我的分析建议:

1. **在 `TeaserSearchService.java` 中修复SQL语法错误**
2. 添加 `teaser_id` 和 `status` 字段到`TeaserSearchResponse` 中，这样可以返回布尔值，    - 如果收藏的teaser已经发布，应该返回false
3. **修复 `teasers/hot` 接口SQL语法错误**
    - `/investor/teasers/hot` 方法调用时改为 `teasers` 对象，使用 `teaserRepository` 查询。
    - **修复 `TeaserSearchService.java` 中`searchTeasers` 方法中，修复SQL语法错误：
    - 添加分页参数验证
    - 检查状态筛选条件
    - 添加排序条件
    - 匃按浏览次数排序
        - **修复TeaserSearchService.java`**第68-76行，修复方法中:
        // 构建排序条件
        Sort.add(cb.like(root.get("status"), TeaserStatus.PUBLISHED)
            }
        }
    }
}
    // 执行搜索
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    if (request.getSortBy() != null && request.getSortOrder().isEmpty()) {
        sort.add(cb.like(root.get("status"), TeaserStatus.PUBLISHED));
            }
        }
    }
}
    // 返回分页数据
    return teaserPage;
}
        return convertToSearchResponse(teaser);
    }
}
```

问题2: `teasers/{teaserId}/viewHistory` 接口返回404
    - **修复：** 在 `TeaserSearchService.java` 中添加 `/investor/teasers/{teaserId}/viewHistory` 知识接口
    - **测试：** 访问 `http://localhost:8080/api/v1/investor/teasers/{teaserId}/viewHistory`，应该返回404

    - **修复建议：** 实现 `viewHistoryRepository` 并在 `TeaserSearchService` 中添加相应的依赖

3. **修复数据一致性**
    - **数据库实体与DTO对应关系**
        - `ViewHistory` 实体需要添加 `teaser_id` 字段
        - `TeaserSearchService` 的 `recordViewActivity` 方法需要注入 `ViewHistoryRepository`
        - 确保 `ViewHistoryRepository` 存在并被正确使用

        - **修复建议：**
            1. **实体类设计问题：**
                - `ViewHistory` 实体类中缺少表名配置
                - `ViewHistoryRepository` 中缺少对应的查询方法
                - 添加 `ViewHistoryRepository` 接口定义

                ```java
                @Repository
                public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
                    void setTeaserId(Long teaserId);
                    void setUserId(Long userId);
                ViewHistory viewHistory = new ViewHistory();
                viewHistory.setTeaserId(teaserId);
                viewHistory.setUserId(userId);
                viewHistoryRepository.save(viewHistory);
                log.info("View history recorded: userId={}, teaserId={});
            }
                ```

            2. **修复Teaser详情接口的404问题**
    - 修复建议：在 `TeaserSearchService.java` 中检查 `teaserRepository.findById(teaserId)` 返回值， 如果为null， 如果考虑使用 `Optional` 返回默认的teaser对象
    - **修复建议：** 在获取Teaser详情方法中，如果 `teaserRepository.findById(teaserId)` 返回null，请先判断是否为null并创建一个默认的Teaser对象返回
    - **修复建议：** 修复 `teasers/search` 接口的SQL语法错误
    - 修复 `teasers/hot` 接口的SQL语法错误
    - 修复 `teasers/recommended` 接口的分页参数默认值问题
    - **修复建议：** 修复以下接口的参数验证和确保正确的数据结构：
    - `/investor/teasers/search`: 确保 `request.getSortOrder()` 不等于null
    - `/investor/teasers/hot`: 确保分页参数有默认值
    - `/investor/teasers/recommended`: 确保分页参数有默认值
    - **修复建议：** 修复 `/investor/teasers` 接口中，`sort` 参数验证应该允许为空（即搜索条件为空时返回全部结果)
    - **修复建议：** 在搜索时，如果没有teaser数据，返回空列表。可以考虑添加初始化测试数据或使用数据导入功能
    - **修复建议：** 在 `TeaserSearchService.java` 中添加测试数据初始化逻辑，如果数据库为空：
    - **修复 `TeaserSearchService.java`**
    ```java
    @Service
    public class TeaserSearchService {

        @Autowired
        private final TeaserRepository teaserRepository;
        @Autowired
        private final ProjectRepository projectRepository;
        @Autowired
        private final ViewHistoryRepository viewHistoryRepository;

        /**
         * 搜索Teaser
         */
        public Page<TeaserSearchResponse> searchTeasers(TeaserSearchRequest request, Long investorId) {
            log.info("Searching teasers for investor: {}", request);

            // 风格化查询条件
            List<Predicate> predicates = new ArrayList<>();

            // 关键词搜索
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                Predicate titlePredicate = cb.like(root.get("title"), "%" + request.getKeyword() + "%");
                predicates.add(cb.like(root.get("aiSummary"), "%" + request.getKeyword() + "%");
            }

            // 地区筛选
            if (request.getRegion() != null && !request.getRegion().isEmpty()) {
                predicates.add(cb.like(root.get("project").get("location"), "%" + request.getRegion() + "%");
            }

            // 行业筛选
            if (request.getIndustry() != null && !request.getIndustry().isEmpty()) {
                predicates.add(root.get("project").get("industry").get("id").in(request.getIndustry()));
            }

            // 融资阶段筛选
            if (request.getFinancingStage() != null && !request.getFinancingStage().isEmpty()) {
                predicates.add(root.get("project").get("financingStage").get("id").in(request.getFinancingStage()));
            }

            // 金额筛选
            if (request.getFinancingAmountMin() != null && request.getFinancingAmountMax() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("project").get("financingAmount"), request.getFinancingAmount()));
            }

            // 排序
            if (request.getSortBy() != null && !request.getSortBy().isEmpty()) {
                predicates.add(cb.asc(root.get("createdAt")));
            }
            if (request.getSortOrder() != null && !request.getSortOrder().isEmpty()) {
                predicates.add(cb.desc());
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }
    }
}
```

问题3: 收藏分组功能未实现
    - **修复：** 在 `InvestorFavoriteService.java` 中修复 `getFavoriteGroups` 方法返回空列表
    - 更新收藏分组接口抛出异常

    - **修复建议：**
        1. 在数据库favorites表中添加group_name字段
        2. 在数据库迁移脚本中创建该字段
        ```sql
        ALTER table favorites add column group_name varchar(255);

        -- 数据库迁移脚本
        insert into database/migrations (version, description, file_name, checksum) values (id,   , created_at, datetime not, modified_at datetime default now())
        where id = 1;

        -- 更新Favorite实体类
        @Entity
        @Table(name = "favorites")
        @Getter
        @Setter
        private Long id;
        private Long userId;
        private Long teaserId;
        private String groupName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
```

问题4: 公共teaser列表返回空列表
    - **修复：** 在测试数据准备脚本中添加测试数据，或者在公共Teaser列表接口返回空列表

    - **修复建议：** 在 `database/test_data.sql` 中添加测试数据
        ```sql
        -- 插入测试数据
        insert into users (id, phone, password, user_type, status) values
        (1, '13800138001', 'Test1234', 'INvertor', , '1000000.00'),
        (2, '13800138002', 'Test1234', 'investor', 1000000.00'),
        (3, '13800138003', 'Test1234', 'Entrepreneur', 1000000.00);

        -- 插入测试项目数据
        insert into projects (id, user_id, project_name, industry, financing_stage, financing_amount, description, location, status, created_at, updated_at)
        values
        (1, '测试项目1', '高科技', 'TECHNOLOGY', 'SERIES_A', 5000000.00, 100000000.00, '北京', 'ACTIVE',        'created_at, now()),

        -- 插入测试Teaser数据
        insert into teasers (id, project_id, title, ai_summary, icon_emoji, company_overview, investment_highlights, core_business, target_market, competitive_advantage, team_size, team_description, status, view_count, favorite_count, created_at)
        values
        (1, '测试Teaser 1', '人工智能解决方案', 'TECHNOLOGY', 'D956e38cbe查看', 'chatgpt-4为我们的AI投资分析师，为您提供专业的投资建议。',
\n
        AI智能分析系统将通过深度学习技术，为投资人提供高质量的投资决策支持。

\n
\n系统特点：\1. 专业性 - 专注于人工智能和科技行业\n2. 数据驱动 - 垍合海量数据和先进算法模型，提供深度洞察\n3. 个性化 - 根据用户偏好生成定制化的推荐\n4. 实时性 - 鳻统持续学习，不断优化算法模型
\n
\n警告：以上示例数据仅用于测试。实际使用时应在测试类中清理或在生产环境中删除。

数据。



4. **Teaser列表接口** - 检查状态筛选条件，如果为空，返回空列表
        - **修复：** 在 `TeaserSearchService.java` 中添加初始化测试数据准备方法，    - **修复建议：** 检查测试环境是否有已发布的Teaser数据
    - **修复建议：**
```java
    @BeforeEach
    public void setUp() {
        // 创建测试项目
        Project project = projectRepository.save(createProject(1L, 22L, userId, PROJECT);
        project.setStatus(Project.ProjectStatus.DRAFT); // 保存项目
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(localDateTime.now());

        // 创建测试Teaser
        Teaser teaser = new Teaser();
        teaser.setProject(project);
        teaser.setTitle("测试Teaser 1 - 人工智能解决方案");
        teaser.setAiSummary("这是一个专注于人工智能领域的创新项目简介");
        teaser.setCompanyOverview("测试公司是一家专注于人工智能领域的创新企业");
        teaser.setInvestmentHighlights("1. 强大的AI分析能力\n2. 数据驱动的商业模式\n3. 鸀化推荐引擎\n4. 预训练NLP模型优化\n5. 高安全可靠的数据处理");
        teaser.setTargetMarket("全球人工智能市场");
        teaser.setCompetitiveAdvantage("技术领先，团队优秀);
        teaser.setTeamSize("50人");
        teaser.setTeamDescription("我们的团队由行业专家和技术大牛组成");
        teaser.setIconEmoji("🤖");
        teaser.setStatus(TeaserStatus.DRAFT); // 保存为草稿
        teaser.setCreatedAt(LocalDateTime.now());
        teaser.setUpdatedAt(localDateTime.now());

        // 保存teaser
        teaser = teaserRepository.save(teaser);

        // 发布Teaser
        teaser.setStatus(TeaserStatus.PUBLISHED);
        teaserRepository.save(teaser);

        return teaser;
    }

    @Test
    void testTeaserSearch() {
        TeaserSearchRequest request = TeaserSearchRequest.builder()
                .page(1)
                .pageSize(10)
                .sortBy("createdAt")
                .sortOrder("desc")
                .build();

        mockMvc -s -X GET "$BASE_URL/investor/teasers" + authorizationToken, + pageSize/10"
                .thenReturn(200, OK)
                .andExpect(teaserSearchResponse.class)
                .andExpect(jsonPath("$.data.items").hasSize(0));
                .andExpect(jsonPath("$.data.total").isGreaterThan(0);
                .andExpect(jsonPath("$.data.page").isEqualTo(1))
                .andExpect(jsonPath("$.data.pageSize").isEqualTo(10));
    }
}
