package com.investment.entity;

import com.investment.enums.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

/**
 * 投资人资料实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investor_profiles")
public class InvestorProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 机构名称
     */
    @Column(name = "institution_name", length = 200)
    private String institutionName;

    /**
     * 职位
     */
    @Column(name = "position", length = 100)
    private String position;

    /**
     * 部门
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * 从业年限
     */
    @Column(name = "work_years")
    private Integer workYears;

    /**
     * 机构类型
     */
    @Column(name = "institution_type", length = 50)
    private String institutionType;

    /**
     * 投资阶段偏好
     */
    @Column(name = "investment_stage", length = 255)
    private String investmentStage;

    /**
     * 投资行业偏好
     */
    @Column(name = "investment_industries", length = 255)
    private String investmentIndustries;

    /**
     * 投资地区偏好
     */
    @Column(name = "investment_region", length = 255)
    private String investmentRegion;

    /**
     * 最小投资金额(万)
     */
    @Column(name = "investment_range_min", precision = 12, scale = 2)
    private BigDecimal investmentRangeMin;

    /**
     * 最大投资金额(万)
     */
    @Column(name = "investment_range_max", precision = 12, scale = 2)
    private BigDecimal investmentRangeMax;

    /**
     * 联系电话
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 微信号
     */
    @Column(name = "wechat", length = 100)
    private String wechat;

    /**
     * 个人/机构简介
     */
    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    /**
     * 投资理念
     */
    @Column(name = "investment_philosophy", columnDefinition = "TEXT")
    private String investmentPhilosophy;

    /**
     * 知名投资案例
     */
    @Column(name = "notable_investments", columnDefinition = "TEXT")
    private String notableInvestments;

    /**
     * 官网URL
     */
    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    /**
     * 领英链接
     */
    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    /**
     * 投资评分（0-100）
     */
    @Column(name = "investment_score")
    @Builder.Default
    private Integer investmentScore = 50;

    /**
     * 活跃度
     */
    @Column(name = "activity_level", length = 20)
    @Builder.Default
    private String activityLevel = "medium";

    /**
     * 回复率(%)
     */
    @Column(name = "response_rate", precision = 5, scale = 2)
    private BigDecimal responseRate;

    /**
     * 平均回复时间（小时）
     */
    @Column(name = "avg_response_time")
    private Integer avgResponseTime;

    /**
     * 投资项目数
     */
    @Column(name = "investment_count")
    @Builder.Default
    private Integer investmentCount = 0;

    /**
     * 关注者数量
     */
    @Column(name = "followers_count")
    @Builder.Default
    private Integer followersCount = 0;

    /**
     * 是否已认证
     */
    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    /**
     * 认证等级
     */
    @Column(name = "verification_level", length = 20)
    @Builder.Default
    private String verificationLevel = "basic";

    /**
     * 投资偏好设置（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "json")
    private String preferences;
}
