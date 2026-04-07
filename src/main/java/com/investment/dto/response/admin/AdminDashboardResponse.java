package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台管理仪表盘响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    /**
     * 总用户数
     */
    private Long totalUsers;

    /**
     * 投资人数量
     */
    private Long investorCount;

    /**
     * 融资用户数量
     */
    private Long entrepreneurCount;

    /**
     * 今日新增用户
     */
    private Long todayNewUsers;

    /**
     * 项目总数
     */
    private Long totalProjects;

    /**
     * 已发布Teaser数量
     */
    private Long publishedTeaserCount;

    /**
     * 待审核认证数量
     */
    private Long pendingVerificationCount;

    /**
     * 待审核申请数量
     */
    private Long pendingApplicationCount;

    /**
     * 总浏览量
     */
    private Long totalViewCount;

    /**
     * 总收藏量
     */
    private Long totalFavoriteCount;

    /**
     * 活跃用户数（最近7天有登录）
     */
    private Long activeUserCount;
}
