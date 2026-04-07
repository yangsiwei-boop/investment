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

/**
 * 项目创建请求DTO
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
    @Size(max = 100, message = "项目名称不能超过100个字符")
    private String name;

    /**
     * 一句话介绍
     */
    @NotBlank(message = "一句话介绍不能为空")
    @Size(max = 200, message = "一句话介绍不能超过200个字符")
    private String summary;

    /**
     * 所属行业
     */
    @NotBlank(message = "所属行业不能为空")
    private String industry;

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
     * 融资用途
     */
    @Size(max = 500, message = "融资用途不能超过500个字符")
    private String financingPurpose;

    /**
     * 公司所在地
     */
    @Size(max = 100, message = "公司所在地不能超过100个字符")
    private String location;

    /**
     * 商业描述
     */
    @Size(max = 5000, message = "商业描述不能超过5000个字符")
    private String businessDescription;

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
     * 竞争优势
     */
    @Size(max = 2000, message = "竞争优势不能超过2000个字符")
    private String competitiveAdvantage;
}
