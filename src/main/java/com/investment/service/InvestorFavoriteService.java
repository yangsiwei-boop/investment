package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.investor.FavoriteRequest;
import com.investment.dto.response.investor.FavoriteResponse;
import com.investment.entity.Favorite;
import com.investment.entity.Project;
import com.investment.entity.Teaser;
import com.investment.enums.TeaserStatus;
import com.investment.repository.FavoriteRepository;
import com.investment.repository.TeaserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    private final FavoriteRepository favoriteRepository;
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
        if (favoriteRepository.existsByUserIdAndTeaserId(investorId, request.getTeaserId())) {
            throw new BusinessException(ErrorCode.ALREADY_FAVORITED);
        }

        // 创建收藏记录
        Favorite favorite = Favorite.builder()
                .userId(investorId)
                .teaserId(request.getTeaserId())
                .build();
        favoriteRepository.save(favorite);

        // 增加收藏计数
        teaser.setFavoriteCount(teaser.getFavoriteCount() + 1);
        teaserRepository.save(teaser);

        return convertToFavoriteResponse(favorite, teaser);
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

        Favorite favorite = favoriteRepository
                .findByUserIdAndTeaserId(investorId, teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);

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
        Page<Favorite> favoritePage = favoriteRepository.findByUserIdOrderByCreatedAtDesc(investorId, pageable);

        return favoritePage.map(favorite -> {
            Teaser teaser = teaserRepository.findById(favorite.getTeaserId()).orElse(null);
            return convertToFavoriteResponse(favorite, teaser);
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
        return favoriteRepository.existsByUserIdAndTeaserId(investorId, teaserId);
    }

    /**
     * 更新收藏分组（暂不支持）
     *
     * @param investorId 投资人ID
     * @param teaserId   Teaser ID
     * @param groupName  分组名
     */
    @Transactional
    public void updateFavoriteGroup(Long investorId, Long teaserId, String groupName) {
        // 数据库favorites表中没有group_name字段，暂不支持分组功能
        log.warn("分组功能暂不支持，需要先在数据库favorites表中添加group_name列");
        throw new BusinessException(ErrorCode.FEATURE_NOT_AVAILABLE, "收藏分组功能暂不支持");
    }

    /**
     * 获取收藏分组列表
     *
     * @param investorId 投资人ID
     * @return 分组列表
     */
    public List<String> getFavoriteGroups(Long investorId) {
        // 数据库favorites表中没有group_name字段，暂不支持分组功能
        log.warn("分组功能暂不支持，需要先在数据库favorites表中添加group_name列");
        return Collections.emptyList();
    }

    /**
     * 获取收藏数量
     *
     * @param investorId 投资人ID
     * @return 收藏数量
     */
    public long getFavoriteCount(Long investorId) {
        return favoriteRepository.countByUserId(investorId);
    }

    /**
     * 转换为收藏响应
     */
    private FavoriteResponse convertToFavoriteResponse(Favorite favorite, Teaser teaser) {
        Project project = teaser != null ? teaser.getProject() : null;

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .teaserId(favorite.getTeaserId())
                .teaserTitle(teaser != null ? teaser.getTitle() : null)
                .teaserSummary(teaser != null ? teaser.getAiSummary() : null)
                .industry(project != null && project.getIndustry() != null ? project.getIndustry().name() : null)
                .financingStage(project != null && project.getFinancingStage() != null ? project.getFinancingStage().name() : null)
                .financingAmount(project != null ? project.getFinancingAmount() : null)
                .groupName(null) // 暂不支持分组
                .note(null) // 暂不支持备注
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}
