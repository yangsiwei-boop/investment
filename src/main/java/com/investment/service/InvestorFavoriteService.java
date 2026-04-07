package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.investor.FavoriteRequest;
import com.investment.dto.response.investor.FavoriteResponse;
import com.investment.entity.InvestorActivity;
import com.investment.entity.Project;
import com.investment.entity.Teaser;
import com.investment.enums.TeaserStatus;
import com.investment.repository.InvestorActivityRepository;
import com.investment.repository.TeaserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 投资人收藏服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestorFavoriteService {

    private final InvestorActivityRepository activityRepository;
    private final TeaserRepository teaserRepository;

    /**
     * 添加收藏
     *
     * @param investorId 投资人ID
     * @param request    收藏请求
     * @return 收藏信息
     */
    @Transactional
    public FavoriteResponse addFavorite(Long investorId, FavoriteRequest request) {
        log.info("Adding favorite for investor: {}, teaser: {}", investorId, request.getTeaserId());

        // 检查Teaser是否存在且已发布
        Teaser teaser = teaserRepository.findById(request.getTeaserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        if (teaser.getStatus() != TeaserStatus.PUBLISHED) {
            throw new BusinessException(ErrorCode.TEASER_NOT_AVAILABLE);
        }

        // 检查是否已收藏
        if (activityRepository.existsByInvestorUserIdAndTeaserIdAndActivityType(
                investorId, request.getTeaserId(), "FAVORITE")) {
            throw new BusinessException(ErrorCode.ALREADY_FAVORITED);
        }

        // 创建收藏记录
        InvestorActivity activity = InvestorActivity.builder()
                .investorUserId(investorId)
                .teaserId(request.getTeaserId())
                .activityType("FAVORITE")
                .groupName(request.getGroupName())
                .notes(request.getNote())
                .build();
        activityRepository.save(activity);

        // 增加收藏计数
        teaser.setFavoriteCount(teaser.getFavoriteCount() + 1);
        teaserRepository.save(teaser);

        return convertToFavoriteResponse(activity, teaser);
    }

    /**
     * 取消收藏
     *
     * @param investorId 投资人ID
     * @param teaserId   Teaser ID
     */
    @Transactional
    public void removeFavorite(Long investorId, Long teaserId) {
        log.info("Removing favorite for investor: {}, teaser: {}", investorId, teaserId);

        InvestorActivity activity = activityRepository
                .findByInvestorUserIdAndTeaserIdAndActivityType(investorId, teaserId, "FAVORITE")
                .orElseThrow(() -> new BusinessException(ErrorCode.FAVORITE_NOT_FOUND));

        activityRepository.delete(activity);

        // 减少收藏计数
        Teaser teaser = teaserRepository.findById(teaserId).orElse(null);
        if (teaser != null && teaser.getFavoriteCount() > 0) {
            teaser.setFavoriteCount(teaser.getFavoriteCount() - 1);
            teaserRepository.save(teaser);
        }
    }

    /**
     * 获取收藏列表
     *
     * @param investorId 投资人ID
     * @param page       页码
     * @param size       每页数量
     * @return 收藏分页列表
     */
    public Page<FavoriteResponse> getFavoriteList(Long investorId, int page, int size) {
        log.info("Getting favorite list for investor: {}", investorId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<InvestorActivity> activityPage = activityRepository
                .findByInvestorUserIdAndActivityTypeOrderByCreatedAtDesc(investorId, "FAVORITE", pageable);

        return activityPage.map(activity -> {
            Teaser teaser = activity.getTeaserId() != null ? teaserRepository.findById(activity.getTeaserId()).orElse(null) : null;
            return convertToFavoriteResponse(activity, teaser);
        });
    }

    /**
     * 检查是否已收藏
     *
     * @param investorId 投资人ID
     * @param teaserId   Teaser ID
     * @return 是否已收藏
     */
    public boolean isFavorited(Long investorId, Long teaserId) {
        return activityRepository.existsByInvestorUserIdAndTeaserIdAndActivityType(
                investorId, teaserId, "FAVORITE");
    }

    /**
     * 更新收藏分组
     *
     * @param investorId 投资人ID
     * @param teaserId   Teaser ID
     * @param groupName  分组名
     */
    @Transactional
    public void updateFavoriteGroup(Long investorId, Long teaserId, String groupName) {
        InvestorActivity activity = activityRepository
                .findByInvestorUserIdAndTeaserIdAndActivityType(investorId, teaserId, "FAVORITE")
                .orElseThrow(() -> new BusinessException(ErrorCode.FAVORITE_NOT_FOUND));

        activity.setGroupName(groupName);
        activityRepository.save(activity);
    }

    /**
     * 获取收藏分组列表
     *
     * @param investorId 投资人ID
     * @return 分组列表
     */
    public List<String> getFavoriteGroups(Long investorId) {
        return activityRepository.findDistinctGroupNamesByInvestorUserIdAndActivityType(
                investorId, "FAVORITE");
    }

    /**
     * 转换为收藏响应
     */
    private FavoriteResponse convertToFavoriteResponse(InvestorActivity activity, Teaser teaser) {
        Project project = teaser != null ? teaser.getProject() : null;

        return FavoriteResponse.builder()
                .id(activity.getId())
                .teaserId(activity.getTeaserId())
                .teaserTitle(teaser != null ? teaser.getTitle() : null)
                .teaserSummary(teaser != null ? teaser.getAiSummary() : null)
                .industry(project != null && project.getIndustry() != null ? project.getIndustry().name() : null)
                .financingStage(project != null && project.getFinancingStage() != null ? project.getFinancingStage().name() : null)
                .financingAmount(project != null ? project.getFinancingAmount() : null)
                .groupName(activity.getGroupName())
                .note(activity.getNotes())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}
