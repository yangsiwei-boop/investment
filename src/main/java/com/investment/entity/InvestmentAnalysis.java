package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * 投资分析实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investment_analyses")
public class InvestmentAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Teaser ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaser_id", nullable = false)
    private Teaser teaser;

    /**
     * 投资人用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_user_id", nullable = false)
    private User investorUser;

    /**
     * 综合评分(0-100)
     */
    @Column(name = "overall_score")
    private Integer overallScore;

    /**
     * 综合评价
     */
    @Column(name = "overall_verdict", length = 20)
    private String overallVerdict;

    /**
     * 行业分析评分
     */
    @Column(name = "industry_analysis_score")
    private Integer industryAnalysisScore;

    /**
     * 行业分析内容
     */
    @Column(name = "industry_analysis_text", columnDefinition = "TEXT")
    private String industryAnalysisText;

    /**
     * 团队分析评分
     */
    @Column(name = "team_analysis_score")
    private Integer teamAnalysisScore;

    /**
     * 团队分析内容
     */
    @Column(name = "team_analysis_text", columnDefinition = "TEXT")
    private String teamAnalysisText;

    /**
     * 技术分析评分
     */
    @Column(name = "technology_analysis_score")
    private Integer technologyAnalysisScore;

    /**
     * 技术分析内容
     */
    @Column(name = "technology_analysis_text", columnDefinition = "TEXT")
    private String technologyAnalysisText;

    /**
     * 竞争力分析评分
     */
    @Column(name = "competitiveness_analysis_score")
    private Integer competitivenessAnalysisScore;

    /**
     * 竞争力分析内容
     */
    @Column(name = "competitiveness_analysis_text", columnDefinition = "TEXT")
    private String competitivenessAnalysisText;

    /**
     * 财务健康度评分
     */
    @Column(name = "financial_health_score")
    private Integer financialHealthScore;

    /**
     * 财务健康度分析内容
     */
    @Column(name = "financial_health_text", columnDefinition = "TEXT")
    private String financialHealthText;

    /**
     * 投资价值评分
     */
    @Column(name = "investment_value_score")
    private Integer investmentValueScore;

    /**
     * 投资价值分析内容
     */
    @Column(name = "investment_value_text", columnDefinition = "TEXT")
    private String investmentValueText;

    /**
     * 投资亮点
     */
    @Column(name = "investment_highlights", columnDefinition = "TEXT")
    private String investmentHighlights;

    /**
     * 风险提示
     */
    @Column(name = "risk_warnings", columnDefinition = "TEXT")
    private String riskWarnings;

    /**
     * 投资建议
     */
    @Column(name = "investment_suggestion", columnDefinition = "TEXT")
    private String investmentSuggestion;

    /**
     * 市场规模描述
     */
    @Column(name = "market_size", length = 255)
    private String marketSize;

    /**
     * 市场增长率
     */
    @Column(name = "market_growth_rate", length = 50)
    private String marketGrowthRate;

    /**
     * 营收数据（JSON格式存储）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "revenue_data", columnDefinition = "json")
    private String revenueData;

    /**
     * 利润率
     */
    @Column(name = "profit_margin", length = 50)
    private String profitMargin;

    /**
     * 付费客户数
     */
    @Column(name = "customer_count")
    private Integer customerCount;

    /**
     * 客户续约率
     */
    @Column(name = "customer_retention_rate", length = 50)
    private String customerRetentionRate;

    /**
     * 数据来源说明
     */
    @Column(name = "data_source", length = 255)
    private String dataSource;

    /**
     * 导出的报告文件URL
     */
    @Column(name = "export_file_url", length = 500)
    private String exportFileUrl;

    /**
     * 是否AI生成
     */
    @Column(name = "is_ai_generated")
    @Builder.Default
    private Boolean isAiGenerated = true;

    /**
     * 分析类型：basic-基础分析,deep-深度分析
     */
    @Column(name = "analysis_type", length = 20)
    @Builder.Default
    private String analysisType = "basic";
}
