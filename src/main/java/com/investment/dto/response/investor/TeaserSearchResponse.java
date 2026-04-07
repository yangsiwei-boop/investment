package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Teaser搜索结果DTO（支持列表包装）
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeaserSearchResponse {

    // ==================== 列表包装字段 ====================

    /**
     * Teaser列表（仅当作为列表响应时使用）
     */
    private List<TeaserSearchResponse> items;

    /**
     * 总数（仅当作为列表响应时使用）
     */
    private Long total;

    /**
     * 当前页（仅当作为列表响应时使用）
     */
    private Integer page;

    /**
     * 每页数量（仅当作为列表响应时使用）
     */
    private Integer pageSize;

    /**
     * 总页数（仅当作为列表响应时使用）
     */
    private Integer totalPages;

    // ==================== 单个Teaser字段 ====================

    /**
     * Teaser ID
     */
    private Long id;

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
     * 公司所在地
     */
    private String location;

    /**
     * 匹配度（0-100）
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
     * 核心亮点（前3个）
     */
    private List<String> highlights;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
