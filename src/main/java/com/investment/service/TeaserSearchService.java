package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.investor.TeaserSearchRequest;
import com.investment.dto.response.investor.TeaserSearchResponse;
import com.investment.dto.response.investor.TeaserDetailResponse;
import com.investment.entity.Project;
import com.investment.entity.Teaser;
import com.investment.entity.ViewHistory;
import com.investment.enums.FinancingStage;
import com.investment.enums.IndustryType;
import com.investment.enums.TeaserStatus;
import com.investment.repository.TeaserRepository;
import com.investment.repository.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Teaser搜索服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeaserSearchService {

    private final TeaserRepository teaserRepository;
    private final ViewHistoryRepository viewHistoryRepository;

    /**
     * 搜索Teaser
     *
     * @param request    搜索请求
     * @param investorId 投资人ID
     * @return 搜索结果
     */
    @Transactional(readOnly = true)
    public Page<TeaserSearchResponse> searchTeasers(TeaserSearchRequest request, Long investorId) {
        log.info("Searching teasers for investor: {}, request: {}", investorId, request);

        // 构建排序
        Sort sort = buildSort(request.getSortBy(), request.getSortOrder());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize(), sort);

        // 构建查询条件
        Specification<Teaser> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 只查询已发布的Teaser
            predicates.add(cb.equal(root.get("status"), TeaserStatus.PUBLISHED));

            // 关键词搜索
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                Predicate titlePredicate = cb.like(root.get("title"), "%" + request.getKeyword() + "%");
                Predicate summaryPredicate = cb.like(root.get("aiSummary"), "%" + request.getKeyword() + "%");
                predicates.add(cb.or(titlePredicate, summaryPredicate));
            }

            // 行业筛选
            if (request.getIndustries() != null && !request.getIndustries().isEmpty()) {
                List<IndustryType> industryEnums = request.getIndustries().stream()
                        .map(IndustryType::fromValue)
                        .filter(java.util.Objects::nonNull)
                        .toList();
                if (!industryEnums.isEmpty()) {
                    predicates.add(root.get("project").get("industry").in(industryEnums));
                }
            }

            // 融资阶段筛选
            if (request.getFinancingStages() != null && !request.getFinancingStages().isEmpty()) {
                List<FinancingStage> stageEnums = request.getFinancingStages().stream()
                        .map(FinancingStage::fromValue)
                        .filter(java.util.Objects::nonNull)
                        .toList();
                if (!stageEnums.isEmpty()) {
                    predicates.add(root.get("project").get("financingStage").in(stageEnums));
                }
            }

            // 地区筛选
            if (request.getRegion() != null && !request.getRegion().isEmpty()) {
                // 通过项目关联查询地区
                predicates.add(cb.like(root.get("project").get("location"), "%" + request.getRegion() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Teaser> teaserPage = teaserRepository.findAll(spec, pageable);

        return teaserPage.map(this::convertToSearchResponse);
    }

    /**
     * 获取Teaser详情
     *
     * @param teaserId   Teaser ID
     * @param investorId 投资人ID
     * @return Teaser详情
     */
    @Transactional
    public TeaserDetailResponse getTeaserDetail(Long teaserId, Long investorId) {
        log.info("Getting teaser detail: {}, investor: {}", teaserId, investorId);

        Teaser teaser = teaserRepository.findById(teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        if (teaser.getStatus() != TeaserStatus.PUBLISHED) {
            throw new BusinessException(ErrorCode.TEASER_NOT_AVAILABLE);
        }

        // 记录浏览活动
        recordViewActivity(investorId, teaserId);

        // 增加浏览次数
        teaser.setViewCount((teaser.getViewCount() != null ? teaser.getViewCount() : 0) + 1);
        teaserRepository.save(teaser);

        return convertToDetailResponse(teaser);
    }

    /**
     * 记录浏览活动
     */
    private void recordViewActivity(Long investorId, Long teaserId) {
        ViewHistory viewHistory = ViewHistory.builder()
                .userId(investorId)
                .teaserId(teaserId)
                .build();
        viewHistoryRepository.save(viewHistory);
    }

    /**
     * 构建排序
     */
    private Sort buildSort(String sortBy, String sortOrder) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String field;
        if (sortBy == null) {
            field = "createdAt";
        } else {
            field = switch (sortBy) {
                case "view_count" -> "viewCount";
                case "match_score" -> "matchScoreAvg";
                case "created_at" -> "createdAt";
                default -> "createdAt";
            };
        }

        return Sort.by(direction, field);
    }

    /**
     * 转换为搜索响应
     */
    private TeaserSearchResponse convertToSearchResponse(Teaser teaser) {
        Project project = teaser.getProject();

        return TeaserSearchResponse.builder()
                .id(teaser.getId())
                .title(teaser.getTitle())
                .summary(teaser.getAiSummary())
                .iconEmoji(teaser.getIconEmoji())
                .industry(project != null && project.getIndustry() != null ? project.getIndustry().name() : null)
                .financingStage(project != null && project.getFinancingStage() != null ? project.getFinancingStage().name() : null)
                .financingAmount(project != null ? project.getFinancingAmount() : null)
                .location(project != null ? project.getLocation() : null)
                .viewCount(teaser.getViewCount())
                .favoriteCount(teaser.getFavoriteCount())
                .createdAt(teaser.getCreatedAt())
                .build();
    }

    /**
     * 转换为详情响应
     */
    private TeaserDetailResponse convertToDetailResponse(Teaser teaser) {
        Project project = teaser.getProject();

        // 将String类型的高亮转换为List
        List<String> highlightsList = parseStringToList(teaser.getInvestmentHighlights());

        return TeaserDetailResponse.builder()
                .id(teaser.getId())
                .projectId(project != null ? project.getId() : null)
                .title(teaser.getTitle())
                .summary(teaser.getAiSummary())
                .description(teaser.getCompanyOverview())
                .iconEmoji(teaser.getIconEmoji())
                .industry(project != null && project.getIndustry() != null ? project.getIndustry().name() : null)
                .financingStage(project != null && project.getFinancingStage() != null ? project.getFinancingStage().name() : null)
                .financingAmount(project != null ? project.getFinancingAmount() : null)
                .financingPurpose(null) // 可从 project 获取
                .location(project != null ? project.getLocation() : null)
                .companyName(teaser.getCompanyName())
                .highlights(highlightsList)
                .businessModel(teaser.getCoreBusiness())
                .targetMarket(null)
                .competitiveAdvantage(teaser.getCompetitiveAdvantage())
                .teamSize(project != null ? project.getTeamSize() : null)
                .coreTeam(teaser.getTeamDescription())
                .viewCount(teaser.getViewCount())
                .favoriteCount(teaser.getFavoriteCount())
                .createdAt(teaser.getCreatedAt())
                .build();
    }

    /**
     * 将换行分隔的字符串转换为列表
     */
    private List<String> parseStringToList(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
