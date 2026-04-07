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
 * 投资人资料更新请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorProfileUpdateRequest {

    /**
     * 投资机构名称
     */
    @Size(max = 100, message = "机构名称不能超过100个字符")
    private String institutionName;

    /**
     * 职位
     */
    @Size(max = 50, message = "职位不能超过50个字符")
    private String position;

    /**
     * 感兴趣的行业
     */
    private List<String> interestedIndustries;

    /**
     * 感兴趣的融资阶段
     */
    private List<String> interestedStages;

    /**
     * 单笔最小投资金额（万元）
     */
    @DecimalMin(value = "0", message = "最小投资金额不能为负数")
    private BigDecimal minInvestmentAmount;

    /**
     * 单笔最大投资金额（万元）
     */
    @DecimalMax(value = "1000000", message = "最大投资金额不能超过100亿元")
    private BigDecimal maxInvestmentAmount;

    /**
     * 感兴趣的地区
     */
    private List<String> interestedRegions;

    /**
     * 投资理念
     */
    @Size(max = 2000, message = "投资理念不能超过2000个字符")
    private String investmentPhilosophy;

    /**
     * 投资案例
     */
    @Size(max = 5000, message = "投资案例不能超过5000个字符")
    private String investmentCases;
}
