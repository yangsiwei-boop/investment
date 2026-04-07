package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工作台统计响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    /**
     * 已浏览Teaser数量
     */
    private Long viewedCount;

    /**
     * 收藏项目数量
     */
    private Long favoriteCount;

    /**
     * 已分析项目数量
     */
    private Long analyzedCount;

    /**
     * 待处理数量
     */
    private Long pendingCount;
}
