package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.entrepreneur.TeaserCreateRequest;
import com.investment.dto.response.entrepreneur.TeaserResponse;
import com.investment.entity.BusinessPlan;
import com.investment.entity.EntrepreneurProfile;
import com.investment.entity.Project;
import com.investment.entity.Teaser;
import com.investment.enums.TeaserStatus;
import com.investment.repository.BusinessPlanRepository;
import com.investment.repository.EntrepreneurProfileRepository;
import com.investment.repository.ProjectRepository;
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
 * 融资用户Teaser服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EntrepreneurTeaserService {

    private final TeaserRepository teaserRepository;
    private final ProjectRepository projectRepository;
    private final EntrepreneurProfileRepository entrepreneurProfileRepository;
    private final BusinessPlanRepository businessPlanRepository;

    /**
     * 创建Teaser
     *
     * @param userId  用户ID
     * @param request 创建请求
     * @return Teaser响应
     */
    @Transactional
    public TeaserResponse createTeaser(Long userId, TeaserCreateRequest request) {
        log.info("Creating teaser for user: {}, project: {}", userId, request.getProjectId());

        // 验证项目
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 检查项目是否已有已发布的Teaser
        if (teaserRepository.existsByProjectIdAndStatus(request.getProjectId(), TeaserStatus.PUBLISHED)) {
            throw new BusinessException(ErrorCode.TEASER_ALREADY_EXISTS);
        }

        // 获取融资用户资料
        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        // 创建Teaser
        Teaser teaser = Teaser.builder()
                .project(project)
                .title(request.getTitle())
                .subtitle(request.getSummary())
                .iconEmoji(request.getIconEmoji())
                .investmentHighlights(convertListToString(request.getHighlights()))
                .coreBusiness(request.getBusinessModel())
                .competitiveAdvantage(request.getCompetitiveAdvantage())
                .teamDescription(request.getTeamIntroduction())
                .status(TeaserStatus.DRAFT)
                .viewCount(0)
                .favoriteCount(0)
                .build();

        teaserRepository.save(teaser);

        return convertToResponse(teaser, project);
    }

    /**
     * 自动生成Teaser（基于项目信息）
     *
     * @param userId    用户ID
     * @param projectId 项目ID
     * @return Teaser响应
     */
    @Transactional
    public TeaserResponse autoGenerateTeaser(Long userId, Long projectId) {
        log.info("Auto-generating teaser for user: {}, project: {}", userId, projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        if (teaserRepository.existsByProjectIdAndStatus(projectId, TeaserStatus.PUBLISHED)) {
            throw new BusinessException(ErrorCode.TEASER_ALREADY_EXISTS);
        }

        // 获取融资用户资料
        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        // 自动生成Teaser内容
        Teaser teaser = Teaser.builder()
                .project(project)
                .title(project.getProjectName() + " - 融资计划")
                .subtitle(project.getOneLineDescription())
                .iconEmoji("🚀")
                .companyOverview(project.getBusinessDescription())
                .coreBusiness(project.getBusinessModel())
                .competitiveAdvantage(project.getCompetitiveAdvantage())
                .status(TeaserStatus.DRAFT)
                .viewCount(0)
                .favoriteCount(0)
                .build();

        teaserRepository.save(teaser);

        return convertToResponse(teaser, project);
    }

    /**
     * 获取Teaser列表
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页数量
     * @return Teaser分页列表
     */
    public Page<TeaserResponse> getTeaserList(Long userId, int page, int size) {
        log.info("Getting teaser list for user: {}", userId);

        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Teaser> teaserPage = teaserRepository.findByEntrepreneurUserId(userId, pageable);

        return teaserPage.map(teaser -> {
            Project project = teaser.getProject();
            return convertToResponse(teaser, project);
        });
    }

    /**
     * 获取Teaser详情
     *
     * @param teaserId Teaser ID
     * @param userId   用户ID
     * @return Teaser详情
     */
    public TeaserResponse getTeaser(Long teaserId, Long userId) {
        log.info("Getting teaser: {}, user: {}", teaserId, userId);

        Teaser teaser = teaserRepository.findById(teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 验证权限
        Project project = teaser.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        return convertToResponse(teaser, project);
    }

    /**
     * 更新Teaser
     *
     * @param teaserId Teaser ID
     * @param userId   用户ID
     * @param request  更新请求
     * @return Teaser响应
     */
    @Transactional
    public TeaserResponse updateTeaser(Long teaserId, Long userId, TeaserCreateRequest request) {
        log.info("Updating teaser: {}, user: {}", teaserId, userId);

        Teaser teaser = teaserRepository.findById(teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 验证权限
        Project project = teaser.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 已发布的Teaser不能修改
        if (teaser.getStatus() == TeaserStatus.PUBLISHED) {
            throw new BusinessException(ErrorCode.CANNOT_MODIFY_PUBLISHED_TEASER);
        }

        teaser.setTitle(request.getTitle());
        teaser.setSubtitle(request.getSummary());
        teaser.setIconEmoji(request.getIconEmoji());
        teaser.setInvestmentHighlights(convertListToString(request.getHighlights()));
        teaser.setCoreBusiness(request.getBusinessModel());
        teaser.setCompetitiveAdvantage(request.getCompetitiveAdvantage());
        teaser.setTeamDescription(request.getTeamIntroduction());

        teaserRepository.save(teaser);

        return convertToResponse(teaser, project);
    }

    /**
     * 发布Teaser
     *
     * @param teaserId Teaser ID
     * @param userId   用户ID
     * @return Teaser响应
     */
    @Transactional
    public TeaserResponse publishTeaser(Long teaserId, Long userId) {
        log.info("Publishing teaser: {}, user: {}", teaserId, userId);

        Teaser teaser = teaserRepository.findById(teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 验证权限
        Project project = teaser.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 验证必填字段
        validateTeaserForPublish(teaser);

        teaser.setStatus(TeaserStatus.PUBLISHED);
        teaser.setGeneratedAt(java.time.LocalDateTime.now());

        teaserRepository.save(teaser);

        return convertToResponse(teaser, project);
    }

    /**
     * 下架Teaser
     *
     * @param teaserId Teaser ID
     * @param userId   用户ID
     * @return Teaser响应
     */
    @Transactional
    public TeaserResponse unpublishTeaser(Long teaserId, Long userId) {
        log.info("Unpublishing teaser: {}, user: {}", teaserId, userId);

        Teaser teaser = teaserRepository.findById(teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 验证权限
        Project project = teaser.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        teaser.setStatus(TeaserStatus.HIDDEN);
        teaserRepository.save(teaser);

        return convertToResponse(teaser, project);
    }

    /**
     * 删除Teaser
     *
     * @param teaserId Teaser ID
     * @param userId   用户ID
     */
    @Transactional
    public void deleteTeaser(Long teaserId, Long userId) {
        log.info("Deleting teaser: {}, user: {}", teaserId, userId);

        Teaser teaser = teaserRepository.findById(teaserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 验证权限
        Project project = teaser.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        teaserRepository.delete(teaser);
    }

    /**
     * 验证Teaser是否可以发布
     */
    private void validateTeaserForPublish(Teaser teaser) {
        if (teaser.getTitle() == null || teaser.getTitle().isEmpty()) {
            throw new BusinessException(ErrorCode.TEASER_TITLE_REQUIRED);
        }
        if (teaser.getSubtitle() == null || teaser.getSubtitle().isEmpty()) {
            throw new BusinessException(ErrorCode.TEASER_SUMMARY_REQUIRED);
        }
    }

    /**
     * 转换为响应DTO
     */
    private TeaserResponse convertToResponse(Teaser teaser, Project project) {
        return TeaserResponse.builder()
                .id(teaser.getId())
                .projectId(project != null ? project.getId() : null)
                .projectName(project != null ? project.getProjectName() : null)
                .title(teaser.getTitle())
                .summary(teaser.getSubtitle())
                .iconEmoji(teaser.getIconEmoji())
                .industry(project != null && project.getIndustry() != null ? project.getIndustry().name() : null)
                .financingStage(project != null && project.getFinancingStage() != null ? project.getFinancingStage().name() : null)
                .financingAmount(project != null ? project.getFinancingAmount() : null)
                .location(project != null ? project.getLocation() : null)
                .highlights(convertStringToList(teaser.getInvestmentHighlights()))
                .businessModel(teaser.getCoreBusiness())
                .competitiveAdvantage(teaser.getCompetitiveAdvantage())
                .teamIntroduction(teaser.getTeamDescription())
                .status(teaser.getStatus() != null ? teaser.getStatus().name() : null)
                .viewCount(teaser.getViewCount())
                .favoriteCount(teaser.getFavoriteCount())
                .createdAt(teaser.getCreatedAt())
                .publishedAt(teaser.getGeneratedAt())
                .build();
    }

    /**
     * 将List<String>转换为换行分隔的String
     */
    private String convertListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join("\n", list);
    }

    /**
     * 将换行分隔的String转换为List<String>
     */
    private List<String> convertStringToList(String str) {
        if (str == null || str.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return java.util.Arrays.stream(str.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
