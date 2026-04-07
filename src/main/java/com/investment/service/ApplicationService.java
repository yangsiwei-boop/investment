package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.application.ApplicationCreateRequest;
import com.investment.dto.response.application.ApplicationResponse;
import com.investment.entity.Application;
import com.investment.entity.Project;
import com.investment.entity.Teaser;
import com.investment.entity.User;
import com.investment.enums.ApplicationStatus;
import com.investment.enums.ApplicationType;
import com.investment.repository.ApplicationRepository;
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

import java.time.LocalDateTime;

/**
 * 申请服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TeaserRepository teaserRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /**
     * 创建申请（投资人）
     *
     * @param investorId 投资人ID
     * @param request    请求
     * @return 申请响应
     */
    @Transactional
    public ApplicationResponse createApplication(Long investorId, ApplicationCreateRequest request) {
        log.info("Creating application for investor: {}, teaser: {}", investorId, request.getTeaserId());

        // 获取投资人
        User investor = userRepository.findById(investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 验证Teaser
        Teaser teaser = teaserRepository.findById(request.getTeaserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 获取项目
        Project project = projectRepository.findById(teaser.getProject().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        // 获取融资用户
        User entrepreneur = project.getEntrepreneurUser();

        // 检查是否已申请
        if (applicationRepository.existsByApplicantIdAndTeaserIdAndApplicationType(
                investorId, request.getTeaserId(), ApplicationType.valueOf(request.getApplicationType()))) {
            throw new BusinessException(ErrorCode.APPLICATION_ALREADY_EXISTS);
        }

        // 创建申请
        Application application = Application.builder()
                .investorUser(investor)
                .project(project)
                .entrepreneurUser(entrepreneur)
                .teaserId(request.getTeaserId())
                .applicationType(ApplicationType.valueOf(request.getApplicationType()))
                .applicationReason(request.getReason())
                .contactInfo(request.getInstitutionName() + " - " + request.getPosition())
                .applicationStatus(ApplicationStatus.PENDING)
                .build();

        applicationRepository.save(application);

        // 发送通知给融资用户
        String typeName = request.getApplicationType().equals("GET_BP") ? "查看BP" : "获取联系方式";

        notificationService.createNotification(
                entrepreneur.getId(),
                com.investment.enums.NotificationType.NEW_APPLICATION,
                "您收到一个新的申请",
                "投资人申请" + typeName + "，项目：" + teaser.getTitle(),
                application.getId(),
                "APPLICATION"
        );

        return convertToResponse(application, teaser, investor);
    }

    /**
     * 获取投资人的申请列表
     *
     * @param investorId 投资人ID
     * @param status     状态筛选
     * @param page       页码
     * @param size       每页数量
     * @return 申请列表
     */
    public Page<ApplicationResponse> getInvestorApplications(Long investorId, String status, int page, int size) {
        log.info("Getting applications for investor: {}", investorId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Application> appPage;
        if (status != null && !status.isEmpty()) {
            appPage = applicationRepository.findByApplicantIdAndStatus(
                    investorId, ApplicationStatus.valueOf(status), pageable);
        } else {
            appPage = applicationRepository.findByApplicantId(investorId, pageable);
        }

        return appPage.map(app -> {
            Teaser teaser = app.getTeaserId() != null ? teaserRepository.findById(app.getTeaserId()).orElse(null) : null;
            User applicant = app.getInvestorUser();
            return convertToResponse(app, teaser, applicant);
        });
    }

    /**
     * 获取融资用户收到的申请列表
     *
     * @param entrepreneurId 融资用户ID
     * @param status         状态筛选
     * @param page           页码
     * @param size           每页数量
     * @return 申请列表
     */
    public Page<ApplicationResponse> getEntrepreneurApplications(Long entrepreneurId, String status, int page, int size) {
        log.info("Getting applications for entrepreneur: {}", entrepreneurId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Application> appPage;
        if (status != null && !status.isEmpty()) {
            appPage = applicationRepository.findByEntrepreneurIdAndStatus(
                    entrepreneurId, ApplicationStatus.valueOf(status), pageable);
        } else {
            appPage = applicationRepository.findByEntrepreneurId(entrepreneurId, pageable);
        }

        return appPage.map(app -> {
            Teaser teaser = app.getTeaserId() != null ? teaserRepository.findById(app.getTeaserId()).orElse(null) : null;
            User applicant = app.getInvestorUser();
            return convertToResponse(app, teaser, applicant);
        });
    }

    /**
     * 审核申请（融资用户）
     *
     * @param applicationId 申请ID
     * @param entrepreneurId 融资用户ID
     * @param approved      是否批准
     * @param comment       审核意见
     * @return 申请响应
     */
    @Transactional
    public ApplicationResponse reviewApplication(Long applicationId, Long entrepreneurId,
                                                  boolean approved, String comment) {
        log.info("Reviewing application: {}, entrepreneur: {}, approved: {}", applicationId, entrepreneurId, approved);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));

        // 验证权限
        if (!application.getEntrepreneurUser().getId().equals(entrepreneurId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 更新状态
        application.setApplicationStatus(approved ? ApplicationStatus.APPROVED : ApplicationStatus.REJECTED);
        application.setRejectionReason(comment);

        User reviewer = userRepository.findById(entrepreneurId).orElse(null);
        application.setReviewedBy(reviewer);
        application.setReviewedAt(LocalDateTime.now());

        applicationRepository.save(application);

        // 发送通知给投资人
        Teaser teaser = application.getTeaserId() != null ? teaserRepository.findById(application.getTeaserId()).orElse(null) : null;
        String typeName = application.getApplicationType().equals(ApplicationType.GET_BP) ? "查看BP" : "获取联系方式";
        String resultText = approved ? "已通过" : "已拒绝";
        String teaserTitle = teaser != null ? teaser.getTitle() : "未知项目";

        notificationService.createNotification(
                application.getInvestorUser().getId(),
                com.investment.enums.NotificationType.APPLICATION_REVIEWED,
                "您的申请" + resultText,
                "您申请" + typeName + "的请求" + resultText + "，项目：" + teaserTitle,
                application.getId(),
                "APPLICATION"
        );

        return convertToResponse(application, teaser, application.getInvestorUser());
    }

    /**
     * 获取申请详情
     *
     * @param applicationId 申请ID
     * @param userId        用户ID
     * @return 申请详情
     */
    public ApplicationResponse getApplication(Long applicationId, Long userId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));

        Teaser teaser = application.getTeaserId() != null ? teaserRepository.findById(application.getTeaserId()).orElse(null) : null;
        User applicant = application.getInvestorUser();

        return convertToResponse(application, teaser, applicant);
    }

    /**
     * 转换为响应DTO
     */
    private ApplicationResponse convertToResponse(Application app, Teaser teaser, User applicant) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .teaserId(app.getTeaserId())
                .teaserTitle(teaser != null ? teaser.getTitle() : null)
                .projectName(teaser != null && teaser.getProject() != null ? teaser.getProject().getProjectName() : null)
                .applicantId(applicant != null ? applicant.getId() : null)
                .applicantName(applicant != null ? applicant.getRealName() : null)
                .applicationType(app.getApplicationType() != null ? app.getApplicationType().name() : null)
                .reason(app.getApplicationReason())
                .institutionName(app.getContactInfo())
                .position(null)
                .status(app.getApplicationStatus() != null ? app.getApplicationStatus().name() : null)
                .reviewComment(app.getRejectionReason())
                .reviewerId(app.getReviewedBy() != null ? app.getReviewedBy().getId() : null)
                .reviewedAt(app.getReviewedAt())
                .createdAt(app.getCreatedAt())
                .build();
    }
}
