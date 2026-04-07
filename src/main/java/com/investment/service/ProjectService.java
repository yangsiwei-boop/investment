package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.entrepreneur.ProjectCreateRequest;
import com.investment.dto.response.entrepreneur.ProjectResponse;
import com.investment.entity.BusinessPlan;
import com.investment.entity.Project;
import com.investment.entity.Teaser;
import com.investment.entity.User;
import com.investment.enums.FinancingStage;
import com.investment.enums.IndustryType;
import com.investment.enums.ProjectStatus;
import com.investment.repository.BusinessPlanRepository;
import com.investment.repository.ProjectRepository;
import com.investment.repository.TeaserRepository;
import com.investment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 项目服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BusinessPlanRepository businessPlanRepository;
    private final TeaserRepository teaserRepository;

    /**
     * 创建项目
     *
     * @param userId  用户ID
     * @param request 创建请求
     * @return 项目响应
     */
    @Transactional
    public ProjectResponse createProject(Long userId, ProjectCreateRequest request) {
        log.info("Creating project for user: {}", userId);

        // 获取融资用户
        User entrepreneur = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 创建项目
        Project project = Project.builder()
                .entrepreneurUser(entrepreneur)
                .projectName(request.getName())
                .oneLineDescription(request.getSummary())
                .industry(parseIndustryType(request.getIndustry()))
                .financingStage(parseFinancingStage(request.getFinancingStage()))
                .financingAmount(request.getFinancingAmount())
                .businessDescription(request.getBusinessDescription())
                .businessModel(request.getBusinessModel())
                .competitiveAdvantage(request.getCompetitiveAdvantage())
                .location(request.getLocation())
                .status(ProjectStatus.DRAFT)
                .build();

        projectRepository.save(project);

        return convertToResponse(project);
    }

    /**
     * 获取项目列表
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页数量
     * @return 项目分页列表
     */
    public Page<ProjectResponse> getProjectList(Long userId, int page, int size) {
        log.info("Getting project list for user: {}", userId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Project> projectPage = projectRepository.findByEntrepreneurUserId(userId, pageable);

        return projectPage.map(this::convertToResponse);
    }

    /**
     * 获取项目详情
     *
     * @param projectId 项目ID
     * @param userId    用户ID
     * @return 项目详情
     */
    public ProjectResponse getProject(Long projectId, Long userId) {
        log.info("Getting project: {}, user: {}", projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        // 验证权限
        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        return convertToResponse(project);
    }

    /**
     * 更新项目
     *
     * @param projectId 项目ID
     * @param userId    用户ID
     * @param request   更新请求
     * @return 项目响应
     */
    @Transactional
    public ProjectResponse updateProject(Long projectId, Long userId, ProjectCreateRequest request) {
        log.info("Updating project: {}, user: {}", projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        // 验证权限
        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 更新项目信息
        project.setProjectName(request.getName());
        project.setOneLineDescription(request.getSummary());
        project.setIndustry(parseIndustryType(request.getIndustry()));
        project.setFinancingStage(parseFinancingStage(request.getFinancingStage()));
        project.setFinancingAmount(request.getFinancingAmount());
        project.setBusinessDescription(request.getBusinessDescription());
        project.setBusinessModel(request.getBusinessModel());
        project.setCompetitiveAdvantage(request.getCompetitiveAdvantage());
        project.setLocation(request.getLocation());

        projectRepository.save(project);

        return convertToResponse(project);
    }

    /**
     * 删除项目
     *
     * @param projectId 项目ID
     * @param userId    用户ID
     */
    @Transactional
    public void deleteProject(Long projectId, Long userId) {
        log.info("Deleting project: {}, user: {}", projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        // 验证权限
        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 软删除 - 更新状态为ARCHIVED
        project.setStatus(ProjectStatus.ARCHIVED);
        projectRepository.save(project);
    }

    /**
     * 转换为响应DTO
     */
    private ProjectResponse convertToResponse(Project project) {
        // 检查是否有BP
        boolean hasBp = businessPlanRepository.existsByProjectId(project.getId());

        // 检查是否有Teaser
        boolean hasTeaser = teaserRepository.existsByProjectId(project.getId());

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .summary(project.getOneLineDescription())
                .industry(project.getIndustry() != null ? project.getIndustry().name() : null)
                .financingStage(project.getFinancingStage() != null ? project.getFinancingStage().name() : null)
                .financingAmount(project.getFinancingAmount())
                .financingPurpose(null) // Project 实体中没有此字段
                .location(project.getLocation())
                .businessDescription(project.getBusinessDescription())
                .businessModel(project.getBusinessModel())
                .targetMarket(null) // Project 实体中没有此字段
                .competitiveAdvantage(project.getCompetitiveAdvantage())
                .status(project.getStatus() != null ? project.getStatus().name() : null)
                .hasBp(hasBp)
                .hasTeaser(hasTeaser)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    /**
     * 解析行业类型
     */
    private IndustryType parseIndustryType(String industry) {
        if (industry == null || industry.isEmpty()) {
            return null;
        }
        try {
            return IndustryType.valueOf(industry);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid industry type: {}", industry);
            return null;
        }
    }

    /**
     * 解析融资阶段
     */
    private FinancingStage parseFinancingStage(String financingStage) {
        if (financingStage == null || financingStage.isEmpty()) {
            return null;
        }
        try {
            return FinancingStage.valueOf(financingStage);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid financing stage: {}", financingStage);
            return null;
        }
    }
}
