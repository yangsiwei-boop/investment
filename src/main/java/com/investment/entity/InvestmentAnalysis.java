package com.investment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投资分析实体
 * 对应数据库 investment_analyses 表
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investment_analyses", indexes = {
        @Index(name = "idx_investment_analyses_teaser_id", columnList = "teaser_id"),
        @Index(name = "idx_investment_analyses_investor_user_id", columnList = "investor_user_id")
})
public class InvestmentAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Teaser ID
     */
    @Column(name = "teaser_id", nullable = false)
    private Long teaserId;

    /**
     * 投资人用户ID
     */
    @Column(name = "investor_user_id", nullable = false)
    private Long investorUserId;

    /**
     * 分析类型
     */
    @Column(name = "analysis_type", length = 50)
    private String analysisType;

    /**
     * 分析内容（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "analysis_content", columnDefinition = "json")
    private String analysisContent;

    /**
     * 评分
     */
    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    /**
     * 推荐意见
     */
    @Column(name = "recommendation", length = 100)
    private String recommendation;

    /**
     * 软删除时间
     */
    @Column(name = "deleted_at")
    @JsonIgnore
    private LocalDateTime deletedAt;
}
