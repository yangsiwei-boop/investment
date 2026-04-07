package com.investment.service;

import com.investment.dto.response.entrepreneur.EntrepreneurDashboardResponse;
import com.investment.entity.EntrepreneurProfile;
import com.investment.enums.TeaserStatus;
import com.investment.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 融资用户工作台服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EntrepreneurDashboardService {

    private final ProjectRepository projectRepository;
    private final TeaserRepository teaserRepository;
    private final BusinessPlanRepository businessPlanRepository;
    private final ApplicationRepository applicationRepository;
    private final QaRecordRepository qaRecordRepository;
    private final EntrepreneurProfileRepository entrepreneurProfileRepository;

    /**
     * 获取工作台统计数据
     *
     * @param userId 用户ID
     * @return 统计数据
     */
    public EntrepreneurDashboardResponse getDashboard(Long userId) {
        log.info("Getting entrepreneur dashboard for user: {}", userId);

        // 统计项目数量
        Long projectCount = projectRepository.countByEntrepreneurUserId(userId);

        // 统计已发布Teaser数量
        Long publishedTeaserCount = teaserRepository.countByEntrepreneurUserIdAndStatus(userId, TeaserStatus.PUBLISHED);

        // 统计待处理申请数量
        Long pendingApplicationCount = applicationRepository.countPendingByUserId(userId);

        // 统计总浏览次数
        Long totalViewCount = teaserRepository.sumViewCountByEntrepreneurUserId(userId);

        // 统计总收藏次数
        Long totalFavoriteCount = teaserRepository.sumFavoriteCountByEntrepreneurUserId(userId);

        // TODO: 需要在BusinessPlanRepository中添加sumDownloadCountByUserId方法
        // 统计BP下载次数 - 暂时返回0，待Repository方法实现后再启用
        Long bpDownloadCount = 0L;

        // TODO: 需要在QaRecordRepository中添加countPendingByUserId方法
        // 统计待回复问题数量 - 暂时返回0，待Repository方法实现后再启用
        Long pendingQuestionCount = 0L;

        return EntrepreneurDashboardResponse.builder()
                .projectCount(projectCount != null ? projectCount : 0L)
                .publishedTeaserCount(publishedTeaserCount != null ? publishedTeaserCount : 0L)
                .pendingApplicationCount(pendingApplicationCount != null ? pendingApplicationCount : 0L)
                .totalViewCount(totalViewCount != null ? totalViewCount : 0L)
                .totalFavoriteCount(totalFavoriteCount != null ? totalFavoriteCount : 0L)
                .bpDownloadCount(bpDownloadCount != null ? bpDownloadCount : 0L)
                .pendingQuestionCount(pendingQuestionCount != null ? pendingQuestionCount : 0L)
                .build();
    }
}
