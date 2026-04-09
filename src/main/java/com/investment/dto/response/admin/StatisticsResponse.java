package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 数据统计响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    /** 概览统计 */
    private OverviewStats overview;

    /** 用户趋势（近7天） */
    private List<TrendItem> userTrend;

    /** 项目趋势（近7天） */
    private List<TrendItem> projectTrend;

    /** 浏览趋势（近7天） */
    private List<TrendItem> viewTrend;

    /** 行业分布 */
    private List<DistributionItem> industryDistribution;

    /** 融资阶段分布 */
    private List<DistributionItem> stageDistribution;

    /**
     * 概览统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverviewStats {

        private Long totalUsers;

        private Long newUsersToday;

        private Long newUsersThisWeek;

        private Long newUsersThisMonth;

        private Long totalProjects;

        private Long newProjectsToday;

        private Long totalTeasers;

        private Long publishedTeasers;

        private Long totalViews;

        private Long totalFavorites;

        private Long totalApplications;

        private Long pendingApplications;
    }

    /**
     * 趋势数据项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendItem {

        private String date;

        private Long value;
    }

    /**
     * 分布数据项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistributionItem {

        private String name;

        private Long value;
    }
}
