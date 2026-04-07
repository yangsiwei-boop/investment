package com.investment.entity;

import com.investment.enums.AnalysisStatus;
import com.investment.enums.TeaserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Teaser实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teasers")
public class Teaser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 项目ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    /**
     * 商业计划书ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_plan_id")
    private BusinessPlan businessPlan;

    /**
     * 标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 封面图片URL
     */
    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    /**
     * 副标题
     */
    @Column(name = "subtitle", length = 500)
    private String subtitle;

    /**
     * 公司名称（匿名时显示为"某公司"）
     */
    @Column(name = "company_name", length = 200)
    private String companyName;

    /**
     * 是否匿名显示公司名称
     */
    @Column(name = "is_anonymous_company")
    @Builder.Default
    private Boolean isAnonymousCompany = true;

    /**
     * 图标emoji
     */
    @Column(name = "icon_emoji", length = 10)
    private String iconEmoji;

    /**
     * AI生成的项目摘要
     */
    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    /**
     * 关键指标数据（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "key_metrics", columnDefinition = "json")
    private String keyMetrics;

    /**
     * 公司概述
     */
    @Column(name = "company_overview", columnDefinition = "TEXT")
    private String companyOverview;

    /**
     * 核心业务
     */
    @Column(name = "core_business", columnDefinition = "TEXT")
    private String coreBusiness;

    /**
     * 团队简介
     */
    @Column(name = "team_description", columnDefinition = "TEXT")
    private String teamDescription;

    /**
     * 产品描述
     */
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    /**
     * 客户案例
     */
    @Column(name = "customer_cases", columnDefinition = "TEXT")
    private String customerCases;

    /**
     * 投资亮点
     */
    @Column(name = "investment_highlights", columnDefinition = "TEXT")
    private String investmentHighlights;

    /**
     * 风险因素
     */
    @Column(name = "risk_factors", columnDefinition = "TEXT")
    private String riskFactors;

    /**
     * 是否需要申请才能查看联系方式
     */
    @Column(name = "contact_permission_required")
    @Builder.Default
    private Boolean contactPermissionRequired = true;

    /**
     * 市场规模
     */
    @Column(name = "market_size", columnDefinition = "TEXT")
    private String marketSize;

    /**
     * 竞争优势
     */
    @Column(name = "competitive_advantage", columnDefinition = "TEXT")
    private String competitiveAdvantage;

    /**
     * 财务数据(JSON格式)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "financial_data", columnDefinition = "json")
    private String financialData;

    /**
     * 融资计划
     */
    @Column(name = "financing_plan", columnDefinition = "TEXT")
    private String financingPlan;

    /**
     * 标签
     */
    @Column(name = "tags", length = 255)
    private String tags;

    /**
     * 浏览次数
     */
    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;

    /**
     * 收藏次数
     */
    @Column(name = "favorite_count")
    @Builder.Default
    private Integer favoriteCount = 0;

    /**
     * 平均匹配度
     */
    @Column(name = "match_score_avg", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal matchScoreAvg = BigDecimal.ZERO;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private TeaserStatus status = TeaserStatus.DRAFT;

    /**
     * AI分析是否已生成
     */
    @Column(name = "ai_analysis_ready")
    @Builder.Default
    private Boolean aiAnalysisReady = false;

    /**
     * 最后AI分析时间
     */
    @Column(name = "last_analyzed_at")
    private LocalDateTime lastAnalyzedAt;

    /**
     * 分享链接
     */
    @Column(name = "sharing_url", length = 255)
    private String sharingUrl;

    /**
     * Teaser过期日期
     */
    @Column(name = "expire_date")
    private LocalDate expireDate;

    /**
     * 生成时间
     */
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
