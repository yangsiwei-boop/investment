package com.investment.dto.response.entrepreneur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Teaser响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeaserResponse {

    /**
     * Teaser ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 标题
     */
    private String title;

    /**
     * 一句话介绍
     */
    private String summary;

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
     * 团队介绍
     */
    private String teamIntroduction;

    /**
     * Teaser状态
     */
    private String status;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 收藏次数
     */
    private Integer favoriteCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;
}
