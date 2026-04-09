package com.investment.entity;

import com.investment.enums.FinancingStage;
import com.investment.enums.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 融资用户资料实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entrepreneur_profiles")
public class EntrepreneurProfile extends BaseEntity {

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
     * 公司名称
     */
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    /**
     * 公司logo URL
     */
    @Column(name = "company_logo_url", length = 500)
    private String companyLogoUrl;

    /**
     * 所属行业
     */
    @Column(name = "industry", length = 50)
    private IndustryType industry;

    /**
     * 营业执照号
     */
    @Column(name = "business_license_number", length = 100)
    private String businessLicenseNumber;

    /**
     * 法定代表人
     */
    @Column(name = "legal_representative", length = 100)
    private String legalRepresentative;

    /**
     * 注册资本(万)
     */
    @Column(name = "registered_capital", precision = 15, scale = 2)
    private BigDecimal registeredCapital;

    /**
     * 公司所在地
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 办公地址
     */
    @Column(name = "office_address", length = 500)
    private String officeAddress;

    /**
     * 公司阶段
     */
    @Column(name = "company_stage", length = 20)
    private FinancingStage companyStage;

    /**
     * 成立时间
     */
    @Column(name = "founded_date")
    private LocalDate foundedDate;

    /**
     * 团队规模
     */
    @Column(name = "team_size")
    private Integer teamSize;

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
     * 营业时间
     */
    @Column(name = "business_hours", length = 100)
    private String businessHours;

    /**
     * 官网URL
     */
    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    /**
     * 社交媒体链接（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "social_media_urls", columnDefinition = "json")
    private String socialMediaUrls;

    /**
     * 公司简介
     */
    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    /**
     * 产品/服务描述
     */
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    /**
     * 目标客户群体
     */
    @Column(name = "target_customers", columnDefinition = "TEXT")
    private String targetCustomers;

    /**
     * 核心优势
     */
    @Column(name = "core_advantages", columnDefinition = "TEXT")
    private String coreAdvantages;

    /**
     * 发展规划
     */
    @Column(name = "development_plans", columnDefinition = "TEXT")
    private String developmentPlans;

    /**
     * 资料完善度 - 数据库中不存在此列
     */
    @Transient
    @Builder.Default
    private Integer profileCompletionRate = 0;
}
