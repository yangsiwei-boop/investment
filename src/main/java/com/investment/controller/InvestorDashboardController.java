package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.investor.DashboardStatsResponse;
import com.investment.dto.response.investor.RecommendedTeaserResponse;
import com.investment.dto.response.investor.RecentActivityResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.InvestorDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资人工作台控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/investor/dashboard")
@RequiredArgsConstructor
@Tag(name = "投资人工作台", description = "投资人工作台相关接口")
@SecurityRequirement(name = "Bearer")
public class InvestorDashboardController {

    private final InvestorDashboardService dashboardService;

    /**
     * 获取工作台首页数据
     *
     * @param principal 当前用户
     * @return 工作台数据
     */
    @GetMapping
    @Operation(summary = "获取工作台首页数据", description = "获取投资人工作台首页统计数据")
    public ApiResponse<Map<String, Object>> getDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting dashboard for investor: {}", principal.getId());

        // 获取统计数据
        DashboardStatsResponse stats = dashboardService.getDashboard(principal.getId());

        // 获取推荐Teaser
        List<RecommendedTeaserResponse> recommendedTeasers = dashboardService.getRecommendedTeasers(principal.getId(), 10);

        // 获取最近活动
        List<RecentActivityResponse> recentActivities = dashboardService.getRecentActivities(principal.getId(), 5);

        Map<String, Object> result = new HashMap<>();
        result.put("stats", stats);
        result.put("recommendedTeasers", recommendedTeasers);
        result.put("recentActivities", recentActivities);

        return ApiResponse.success(result);
    }

    /**
     * 获取统计数据
     *
     * @param principal 当前用户
     * @return 统计数据
     */
    @GetMapping("/stats")
    @Operation(summary = "获取统计数据", description = "获取投资人统计数据")
    public ApiResponse<DashboardStatsResponse> getStats(@AuthenticationPrincipal UserPrincipal principal) {
        DashboardStatsResponse stats = dashboardService.getDashboard(principal.getId());
        return ApiResponse.success(stats);
    }

    /**
     * 获取推荐项目列表
     *
     * @param principal 当前用户
     * @param limit 数量限制
     * @return 推荐项目列表
     */
    @GetMapping("/recommended")
    @Operation(summary = "获取推荐项目", description = "获取为投资人推荐的项目列表")
    public ApiResponse<List<RecommendedTeaserResponse>> getRecommendedTeasers(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<RecommendedTeaserResponse> teasers = dashboardService.getRecommendedTeasers(principal.getId(), limit);
        return ApiResponse.success(teasers);
    }

    /**
     * 获取最近活动记录
     *
     * @param principal 当前用户
     * @param limit 数量限制
     * @return 最近活动列表
     */
    @GetMapping("/activities")
    @Operation(summary = "获取最近活动", description = "获取投资人最近的活动记录")
    public ApiResponse<List<RecentActivityResponse>> getRecentActivities(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<RecentActivityResponse> activities = dashboardService.getRecentActivities(principal.getId(), limit);
        return ApiResponse.success(activities);
    }
}
