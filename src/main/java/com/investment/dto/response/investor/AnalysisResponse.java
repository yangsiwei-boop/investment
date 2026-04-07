package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI投资分析响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {

    /**
     * 分析ID
     */
    private Long id;

    /**
     * Teaser ID
     */
    private Long teaserId;

    /**
     * Teaser标题
     */
    private String teaserTitle;

    /**
     * 分析状态
     */
    private String status;

    /**
     * 综合评分（0-100）
     */
    private Integer overallScore;

    /**
     * 评分等级（A/B/C/D/E或excellent/good/average/below_average/poor）
     */
    private String scoreGrade;

    /**
     * 市场评分
     */
    private Integer marketScore;

    /**
     * 团队评分
     */
    private Integer teamScore;

    /**
     * 产品评分
     */
    private Integer productScore;

    /**
     * 商业模式评分
     */
    private Integer businessModelScore;

    /**
     * 财务评分
     */
    private Integer financialScore;

    /**
     * 竞争力评分
     */
    private Integer competitivenessScore;

    /**
     * 风险评估（Map格式）
     */
    private Map<String, Object> riskAssessment;

    /**
     * 投资亮点（字符串格式，换行分隔）
     */
    private String highlights;

    /**
     * 风险提示（字符串格式，换行分隔）
     */
    private String risks;

    /**
     * 详细分析
     */
    private Map<String, Object> detailedAnalysis;

    /**
     * 投资建议
     */
    private String investmentAdvice;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 风险评估内部类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskAssessment {
        /**
         * 风险等级（low/medium/high）
         */
        private String riskLevel;

        /**
         * 市场风险
         */
        private String marketRisk;

        /**
         * 竞争风险
         */
        private String competitiveRisk;

        /**
         * 财务风险
         */
        private String financialRisk;

        /**
         * 团队风险
         */
        private String teamRisk;

        /**
         * 风险评分（0-100，越高风险越大）
         */
        private Integer riskScore;
    }
}
