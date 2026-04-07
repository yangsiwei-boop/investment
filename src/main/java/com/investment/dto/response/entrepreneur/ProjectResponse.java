package com.investment.dto.response.entrepreneur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 一句话介绍
     */
    private String summary;

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
     * 融资用途
     */
    private String financingPurpose;

    /**
     * 公司所在地
     */
    private String location;

    /**
     * 商业描述
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
