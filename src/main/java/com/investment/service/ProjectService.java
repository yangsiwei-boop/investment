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

import java.util.Collections;
import java.util.List;

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

        User entrepreneur = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Project project = Project.builder()
                .entrepreneurUser(entrepreneur)
                .projectName(request.getProjectName())
                .oneLineDescription(request.getOneLineDescription())
                .industry(IndustryType.fromValue(request.getIndustry()))
                .businessDescription(request.getBusinessDescription())
                .location(request.getLocation())
                .companyFoundedDate(request.getCompanyFoundedDate())
                .teamSize(request.getTeamSize())
                .officeAddress(request.getOfficeAddress())
                .companyWebsite(request.getCompanyWebsite())
                .financingStage(FinancingStage.fromValue(request.getFinancingStage()))
                .financingAmount(request.getFinancingAmount())
                .equityPercentage(request.getEquityPercentage())
                .financingHistory(request.getFinancingHistory())
                .marketSize(request.getMarketSize())
                .competitiveAdvantage(request.getCompetitiveAdvantage())
                .businessModel(request.getBusinessModel())
                .targetMarket(request.getTargetMarket())
                .revenueYtd(request.getRevenueYtd())
                .revenueLastYear(request.getRevenueLastYear())
                .grossMargin(request.getGrossMargin())
                .contactPerson(request.getContactPerson())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .isAnonymous(request.getIsAnonymous() != null ? request.getIsAnonymous() : true)
                .iconEmoji(request.getIconEmoji())
                .tags(convertTagsToString(request.getTags()))
                .status(resolveStatus(request.getStatus()))
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long projectId, Long userId) {
        log.info("Getting project: {}, user: {}", projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

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

        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 更新所有字段（前端传什么更新什么）
        if (request.getProjectName() != null) {
            project.setProjectName(request.getProjectName());
        }
        if (request.getOneLineDescription() != null) {
            project.setOneLineDescription(request.getOneLineDescription());
        }
        if (request.getIndustry() != null) {
            project.setIndustry(IndustryType.fromValue(request.getIndustry()));
        }
        if (request.getBusinessDescription() != null) {
            project.setBusinessDescription(request.getBusinessDescription());
        }
        if (request.getLocation() != null) {
            project.setLocation(request.getLocation());
        }
        if (request.getCompanyFoundedDate() != null) {
            project.setCompanyFoundedDate(request.getCompanyFoundedDate());
        }
        if (request.getTeamSize() != null) {
            project.setTeamSize(request.getTeamSize());
        }
        if (request.getOfficeAddress() != null) {
            project.setOfficeAddress(request.getOfficeAddress());
        }
        if (request.getCompanyWebsite() != null) {
            project.setCompanyWebsite(request.getCompanyWebsite());
        }
        if (request.getFinancingStage() != null) {
            project.setFinancingStage(FinancingStage.fromValue(request.getFinancingStage()));
        }
        if (request.getFinancingAmount() != null) {
            project.setFinancingAmount(request.getFinancingAmount());
        }
        if (request.getEquityPercentage() != null) {
            project.setEquityPercentage(request.getEquityPercentage());
        }
        if (request.getFinancingHistory() != null) {
            project.setFinancingHistory(request.getFinancingHistory());
        }
        if (request.getMarketSize() != null) {
            project.setMarketSize(request.getMarketSize());
        }
        if (request.getCompetitiveAdvantage() != null) {
            project.setCompetitiveAdvantage(request.getCompetitiveAdvantage());
        }
        if (request.getBusinessModel() != null) {
            project.setBusinessModel(request.getBusinessModel());
        }
        if (request.getTargetMarket() != null) {
            project.setTargetMarket(request.getTargetMarket());
        }
        if (request.getRevenueYtd() != null) {
            project.setRevenueYtd(request.getRevenueYtd());
        }
        if (request.getRevenueLastYear() != null) {
            project.setRevenueLastYear(request.getRevenueLastYear());
        }
        if (request.getGrossMargin() != null) {
            project.setGrossMargin(request.getGrossMargin());
        }
        if (request.getContactPerson() != null) {
            project.setContactPerson(request.getContactPerson());
        }
        if (request.getContactPhone() != null) {
            project.setContactPhone(request.getContactPhone());
        }
        if (request.getContactEmail() != null) {
            project.setContactEmail(request.getContactEmail());
        }
        if (request.getIsAnonymous() != null) {
            project.setIsAnonymous(request.getIsAnonymous());
        }
        if (request.getIconEmoji() != null) {
            project.setIconEmoji(request.getIconEmoji());
        }
        if (request.getTags() != null) {
            project.setTags(convertTagsToString(request.getTags()));
        }
        if (request.getStatus() != null) {
            project.setStatus(resolveStatus(request.getStatus()));
        }

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

        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        project.setStatus(ProjectStatus.ARCHIVED);
        projectRepository.save(project);
    }

    /**
     * 转换为响应DTO
     */
    private ProjectResponse convertToResponse(Project project) {
        boolean hasBp = businessPlanRepository.existsByProjectId(project.getId());
        boolean hasTeaser = teaserRepository.existsByProjectId(project.getId());

        return ProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .oneLineDescription(project.getOneLineDescription())
                .industry(project.getIndustry() != null ? project.getIndustry().getCode() : null)
                .financingStage(project.getFinancingStage() != null ? project.getFinancingStage().getCode() : null)
                .financingAmount(project.getFinancingAmount())
                .businessDescription(project.getBusinessDescription())
                .businessModel(project.getBusinessModel())
                .targetMarket(project.getTargetMarket())
                .competitiveAdvantage(project.getCompetitiveAdvantage())
                .location(project.getLocation())
                .companyFoundedDate(project.getCompanyFoundedDate())
                .officeAddress(project.getOfficeAddress())
                .companyWebsite(project.getCompanyWebsite())
                .teamSize(project.getTeamSize())
                .equityPercentage(project.getEquityPercentage())
                .financingHistory(project.getFinancingHistory())
                .marketSize(project.getMarketSize())
                .revenueYtd(project.getRevenueYtd())
                .revenueLastYear(project.getRevenueLastYear())
                .grossMargin(project.getGrossMargin())
                .contactPerson(project.getContactPerson())
                .contactPhone(project.getContactPhone())
                .contactEmail(project.getContactEmail())
                .isAnonymous(project.getIsAnonymous())
                .iconEmoji(project.getIconEmoji())
                .tags(convertStringToTags(project.getTags()))
                .status(project.getStatus() != null ? project.getStatus().name() : null)
                .hasBp(hasBp)
                .hasTeaser(hasTeaser)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    /**
     * 解析项目状态
     */
    private ProjectStatus resolveStatus(String status) {
        if (status == null || status.isEmpty()) {
            return ProjectStatus.DRAFT;
        }
        try {
            return ProjectStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid project status: {}, defaulting to DRAFT", status);
            return ProjectStatus.DRAFT;
        }
    }

    /**
     * 将标签列表转为 JSON 字符串存储
     */
    private String convertTagsToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tags.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("\"").append(tags.get(i).replace("\"", "\\\"")).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 将 JSON 字符串转为标签列表
     */
    private List<String> convertStringToTags(String tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        // 简单解析 ["tag1","tag2"] 格式
        String content = tags.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1);
        }
        if (content.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> result = new java.util.ArrayList<>();
        for (String tag : content.split(",")) {
            String cleaned = tag.trim().replace("\"", "");
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }
        return result;
    }
}
