package com.investment.dto.request.entrepreneur;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 项目创建/更新请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 200, message = "项目名称不能超过200个字符")
    private String projectName;

    /**
     * 所属行业
     */
    @NotBlank(message = "所属行业不能为空")
    private String industry;

    /**
     * 一句话描述（≤50字）
     */
    @NotBlank(message = "一句话描述不能为空")
    @Size(max = 500, message = "一句话描述不能超过500个字符")
    private String oneLineDescription;

    /**
     * 业务描述
     */
    @NotBlank(message = "业务描述不能为空")
    @Size(max = 5000, message = "业务描述不能超过5000个字符")
    private String businessDescription;

    /**
     * 项目所在地
     */
    @Size(max = 100, message = "项目所在地不能超过100个字符")
    private String location;

    /**
     * 成立时间
     */
    private LocalDate companyFoundedDate;

    /**
     * 团队规模
     */
    private Integer teamSize;

    /**
     * 办公地址
     */
    @Size(max = 500, message = "办公地址不能超过500个字符")
    private String officeAddress;

    /**
     * 公司官网
     */
    @Size(max = 255, message = "公司官网不能超过255个字符")
    private String companyWebsite;

    /**
     * 融资阶段
     */
    @NotBlank(message = "融资阶段不能为空")
    private String financingStage;

    /**
     * 融资金额（万元）
     */
    @DecimalMin(value = "0", message = "融资金额不能为负数")
    @DecimalMax(value = "1000000", message = "融资金额不能超过100亿元")
    private BigDecimal financingAmount;

    /**
     * 出让股权比例（%）
     */
    @DecimalMin(value = "0", message = "股权比例不能为负数")
    @DecimalMax(value = "100", message = "股权比例不能超过100%")
    private BigDecimal equityPercentage;

    /**
     * 融资历史
     */
    private String financingHistory;

    /**
     * 市场规模描述
     */
    private String marketSize;

    /**
     * 竞争优势
     */
    @Size(max = 2000, message = "竞争优势不能超过2000个字符")
    private String competitiveAdvantage;

    /**
     * 商业模式
     */
    @Size(max = 2000, message = "商业模式不能超过2000个字符")
    private String businessModel;

    /**
     * 目标市场
     */
    @Size(max = 1000, message = "目标市场不能超过1000个字符")
    private String targetMarket;

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
    @Size(max = 100, message = "联系人不能超过100个字符")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话不能超过20个字符")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Size(max = 100, message = "联系邮箱不能超过100个字符")
    private String contactEmail;

    /**
     * 是否匿名显示，默认 true
     */
    private Boolean isAnonymous;

    /**
     * 图标表情
     */
    @Size(max = 10, message = "图标表情不能超过10个字符")
    private String iconEmoji;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 状态，默认 DRAFT
     */
    private String status;
}
