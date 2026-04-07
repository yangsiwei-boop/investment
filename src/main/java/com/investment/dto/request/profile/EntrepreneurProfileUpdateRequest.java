package com.investment.dto.request.profile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 融资用户资料更新请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurProfileUpdateRequest {

    /**
     * 公司名称
     */
    @Size(max = 100, message = "公司名称不能超过100个字符")
    private String companyName;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 融资阶段
     */
    private String financingStage;

    /**
     * 公司所在地
     */
    @Size(max = 100, message = "公司所在地不能超过100个字符")
    private String location;

    /**
     * 公司规模
     */
    private String companySize;

    /**
     * 公司简介
     */
    @Size(max = 2000, message = "公司简介不能超过2000个字符")
    private String companyIntroduction;

    /**
     * 核心业务
     */
    @Size(max = 1000, message = "核心业务不能超过1000个字符")
    private String coreBusiness;

    /**
     * 目标融资金额（万元）
     */
    @DecimalMin(value = "0", message = "融资金额不能为负数")
    @DecimalMax(value = "1000000", message = "融资金额不能超过100亿元")
    private BigDecimal targetFinancingAmount;

    /**
     * 公司官网
     */
    @Size(max = 200, message = "官网地址不能超过200个字符")
    private String website;

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
}
