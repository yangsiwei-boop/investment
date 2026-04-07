package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Teaser详情响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeaserDetailResponse {

    /**
     * Teaser ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 标题
     */
    private String title;

    /**
     * 一句话介绍
     */
    private String summary;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 图标emoji
     */
    private String iconEmoji;

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
     * 公司名称
     */
    private String companyName;

    /**
     * 核心亮点
     */
    private List<String> highlights;

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
     * 团队规模
     */
    private Integer teamSize;

    /**
     * 核心团队简介
     */
    private String coreTeam;

    /**
     * 关键指标
     */
    private String keyMetrics;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 收藏次数
     */
    private Integer favoriteCount;

    /**
     * 是否已收藏
     */
    private Boolean isFavorite;

    /**
     * 是否已申请
     */
    private Boolean hasApplied;

    /**
     * 是否有BP
     */
    private Boolean hasBp;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
