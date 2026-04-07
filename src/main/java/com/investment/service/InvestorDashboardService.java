package com.investment.service;

import com.investment.dto.response.investor.DashboardStatsResponse;
import com.investment.dto.response.investor.RecommendedTeaserResponse;
import com.investment.dto.response.investor.RecentActivityResponse;
import com.investment.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投资人工作台服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestorDashboardService {

    private final UserRepository userRepository;
    private final TeaserRepository teaserRepository;
    private final ApplicationRepository applicationRepository;
    private final QaRecordRepository qaRecordRepository;
    private final InvestmentAnalysisRepository investmentAnalysisRepository;

    /**
     * 获取投资人工作台首页数据
     *
     * @param userId 用户ID
     * @return 工作台数据
     */
    public DashboardStatsResponse getDashboard(Long userId) {
        log.info("Getting investor dashboard for userId: {}", userId);

        // 统计数据
        Long viewedCount = countViewedTeasers(userId);
        Long favoriteCount = countFavorites(userId);
        Long analyzedCount = countAnalyzedProjects(userId);
        Long pendingCount = countPendingApplications(userId);

        return DashboardStatsResponse.builder()
                .viewedCount(viewedCount)
                .favoriteCount(favoriteCount)
                .analyzedCount(analyzedCount)
                .pendingCount(pendingCount)
                .build();
    }

    /**
     * 获取推荐Teaser列表
     *
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 推荐Teaser列表
     */
    public List<RecommendedTeaserResponse> getRecommendedTeasers(Long userId, int limit) {
        log.info("Getting recommended teasers for userId: {}, limit: {}", userId, limit);
        // TODO: 实现基于用户偏好的推荐逻辑
        // 目前返回最新的已发布Teaser
        return List.of();
    }

    /**
     * 获取最近活动记录
     *
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 最近活动列表
     */
    public List<RecentActivityResponse> getRecentActivities(Long userId, int limit) {
        log.info("Getting recent activities for userId: {}, limit: {}", userId, limit);
        // TODO: 实现最近活动记录查询
        return List.of();
    }

    /**
     * 统计已浏览的Teaser数量
     */
    private Long countViewedTeasers(Long userId) {
        // TODO: 实现浏览统计
        return 0L;
    }

    /**
     * 统计收藏数量
     */
    private Long countFavorites(Long userId) {
        // TODO: 实现收藏统计
        return 0L;
    }

    /**
     * 统计已分析项目数量
     */
    private Long countAnalyzedProjects(Long userId) {
        return investmentAnalysisRepository.countByInvestorUserId(userId);
    }

    /**
     * 统计待处理申请数量
     */
    private Long countPendingApplications(Long userId) {
        // TODO: 实现待处理申请统计
        return 0L;
    }
}
