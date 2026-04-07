package com.investment.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 投资人资料响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorProfileResponse {

    /**
     * 资料ID
     */
    private Long id;

    /**
     * 投资机构名称
     */
    private String institutionName;

    /**
     * 职位
     */
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
    private BigDecimal minInvestmentAmount;

    /**
     * 单笔最大投资金额（万元）
     */
    private BigDecimal maxInvestmentAmount;

    /**
     * 感兴趣的地区
     */
    private List<String> interestedRegions;

    /**
     * 投资理念
     */
    private String investmentPhilosophy;

    /**
     * 投资案例
     */
    private String investmentCases;

    /**
     * 认证状态
     */
    private String verificationStatus;

    /**
     * 资料完整度
     */
    private Integer completeness;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
