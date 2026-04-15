package com.investment.dto.response.entrepreneur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 一句话描述
     */
    private String oneLineDescription;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 融资阶段
     */
    private String financingStage;

    /**
     * 融资金额（万元）
     */
    private BigDecimal financingAmount;

    /**
     * 业务描述
     */
    private String businessDescription;

    /**
     * 商业模式
     */
    private String businessModel;

    /**
     * 目标市场
     */
    private String targetMarket;

    /**
     * 竞争优势
     */
    private String competitiveAdvantage;

    /**
     * 项目所在地
     */
    private String location;

    /**
     * 公司成立日期
     */
    private LocalDate companyFoundedDate;

    /**
     * 办公地址
     */
    private String officeAddress;

    /**
     * 公司官网
     */
    private String companyWebsite;

    /**
     * 团队规模
     */
    private Integer teamSize;

    /**
     * 出让股权比例（%）
     */
    private BigDecimal equityPercentage;

    /**
     * 融资历史
     */
    private String financingHistory;

    /**
     * 市场规模
     */
    private String marketSize;

    /**
     * 当年营收（万元）
     */
    private BigDecimal revenueYtd;

    /**
     * 去年营收（万元）
     */
    private BigDecimal revenueLastYear;

    /**
     * 毛利率（%）
     */
    private BigDecimal grossMargin;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 是否匿名显示
     */
    private Boolean isAnonymous;

    /**
     * 图标表情
     */
    private String iconEmoji;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 项目状态
     */
    private String status;

    /**
     * 是否有BP
     */
    private Boolean hasBp;

    /**
     * 是否有Teaser
     */
    private Boolean hasTeaser;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
