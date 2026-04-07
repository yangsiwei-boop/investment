package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收藏响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {

    /**
     * 收藏ID
     */
    private Long id;

    /**
     * Teaser ID
     */
    private Long teaserId;

    /**
     * Teaser标题
     */
    private String teaserTitle;

    /**
     * Teaser简介
     */
    private String teaserSummary;

    /**
     * 图标emoji
     */
    private String iconEmoji;

    /**
     * 行业
     */
    private String industry;

    /**
     * 融资阶段
     */
    private String financingStage;

    /**
     * 融资金额
     */
    private BigDecimal financingAmount;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 备注
     */
    private String note;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 收藏时间
     */
    private LocalDateTime createdAt;
}
