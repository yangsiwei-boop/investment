package com.investment.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 融资用户资料响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurProfileResponse {

    /**
     * 资料ID
     */
    private Long id;

    /**
     * 公司名称
     */
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
    private String location;

    /**
     * 公司规模
     */
    private String companySize;

    /**
     * 公司简介
     */
    private String companyIntroduction;

    /**
     * 核心业务
     */
    private String coreBusiness;

    /**
     * 目标融资金额（万元）
     */
    private BigDecimal targetFinancingAmount;

    /**
     * 公司官网
     */
    private String website;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

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
