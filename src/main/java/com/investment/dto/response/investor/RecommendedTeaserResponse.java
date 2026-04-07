package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐Teaser响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedTeaserResponse {

    /**
     * Teaser ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 项目简介
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
     * 匹配度(0-100)
     */
    private Integer matchScore;

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
     * 标签列表
     */
    private List<String> tags;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
