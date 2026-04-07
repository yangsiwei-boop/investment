package com.investment.entity;

import com.investment.enums.FinancingStage;
import com.investment.enums.IndustryType;
import com.investment.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 融资用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepreneur_user_id", nullable = false)
    private User entrepreneurUser;

    /**
     * 项目名称
     */
    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName;

    /**
     * 项目编码
     */
    @Column(name = "project_code", length = 50, unique = true)
    private String projectCode;

    /**
     * 所属行业
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "industry", length = 50)
    private IndustryType industry;

    /**
     * 项目所在地
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 公司成立日期
     */
    @Column(name = "company_founded_date")
    private LocalDate companyFoundedDate;

    /**
     * 办公地址
     */
    @Column(name = "office_address", length = 500)
    private String officeAddress;

    /**
     * 公司官网
     */
    @Column(name = "company_website", length = 255)
    private String companyWebsite;

    /**
     * 融资阶段
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "financing_stage", length = 20)
    private FinancingStage financingStage;

    /**
     * 融资金额(万)
     */
    @Column(name = "financing_amount", precision = 12, scale = 2)
    private BigDecimal financingAmount;

    /**
     * 估值(万)
     */
    @Column(name = "valuation", precision = 15, scale = 2)
    private BigDecimal valuation;

    /**
     * 出让股权比例(%)
     */
    @Column(name = "equity_percentage", precision = 5, scale = 2)
    private BigDecimal equityPercentage;

    /**
     * 融资历史
     */
    @Column(name = "financing_history", columnDefinition = "TEXT")
    private String financingHistory;

    /**
     * 一句话描述
     */
    @Column(name = "one_line_description", length = 500)
    private String oneLineDescription;

    /**
     * 业务描述
     */
    @Column(name = "business_description", columnDefinition = "TEXT")
    private String businessDescription;

    /**
     * 公司logo URL
     */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /**
     * 商业计划书文件URL
     */
    @Column(name = "business_plan_file_url", length = 500)
    private String businessPlanFileUrl;

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
     * 联系人
     */
    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    /**
     * 商业模式
     */
    @Column(name = "business_model", columnDefinition = "TEXT")
    private String businessModel;

    /**
     * 当年营收(万)
     */
    @Column(name = "revenue_ytd", precision = 12, scale = 2)
    private BigDecimal revenueYtd;

    /**
     * 去年营收(万)
     */
    @Column(name = "revenue_last_year", precision = 12, scale = 2)
    private BigDecimal revenueLastYear;

    /**
     * 毛利率(%)
     */
    @Column(name = "gross_margin", precision = 5, scale = 2)
    private BigDecimal grossMargin;

    /**
     * 团队规模
     */
    @Column(name = "team_size")
    private Integer teamSize;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.DRAFT;

    /**
     * 是否匿名显示
     */
    @Column(name = "is_anonymous")
    @Builder.Default
    private Boolean isAnonymous = true;

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
}
