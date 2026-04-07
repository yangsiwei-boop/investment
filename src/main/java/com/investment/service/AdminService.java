package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.admin.UserStatusUpdateRequest;
import com.investment.dto.request.admin.VerificationReviewRequest;
import com.investment.dto.response.admin.AdminDashboardResponse;
import com.investment.dto.response.admin.UserListResponse;
import com.investment.dto.response.admin.VerificationDetailResponse;
import com.investment.entity.User;
import com.investment.entity.UserVerification;
import com.investment.enums.UserStatus;
import com.investment.enums.VerificationStatus;
import com.investment.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 后台管理服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final InvestorProfileRepository investorProfileRepository;
    private final EntrepreneurProfileRepository entrepreneurProfileRepository;
    private final ProjectRepository projectRepository;
    private final TeaserRepository teaserRepository;
    private final UserVerificationRepository verificationRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * 获取仪表盘数据
     *
     * @return 仪表盘数据
     */
    public AdminDashboardResponse getDashboard() {
        log.info("Getting admin dashboard data");

        // 统计用户数量
        Long totalUsers = userRepository.count();
        Long investorCount = investorProfileRepository.count();
        Long entrepreneurCount = entrepreneurProfileRepository.count();

        // 今日新增用户
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        Long todayNewUsers = userRepository.countByCreatedAtAfter(todayStart);

        // 项目统计
        Long totalProjects = projectRepository.count();

        // Teaser统计
        Long publishedTeaserCount = teaserRepository.countByStatus(com.investment.enums.TeaserStatus.PUBLISHED);

        // 待审核数量
        Long pendingVerificationCount = verificationRepository.countByVerificationStatus(VerificationStatus.PENDING);
        Long pendingApplicationCount = applicationRepository.countByApplicationStatus(com.investment.enums.ApplicationStatus.PENDING);

        // 浏览和收藏统计
        Long totalViewCount = teaserRepository.sumAllViewCount();
        Long totalFavoriteCount = teaserRepository.sumAllFavoriteCount();

        // 活跃用户（最近7天）
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        Long activeUserCount = userRepository.countByLastLoginAtAfter(weekAgo);

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .investorCount(investorCount)
                .entrepreneurCount(entrepreneurCount)
                .todayNewUsers(todayNewUsers)
                .totalProjects(totalProjects)
                .publishedTeaserCount(publishedTeaserCount)
                .pendingVerificationCount(pendingVerificationCount)
                .pendingApplicationCount(pendingApplicationCount)
                .totalViewCount(totalViewCount != null ? totalViewCount : 0L)
                .totalFavoriteCount(totalFavoriteCount != null ? totalFavoriteCount : 0L)
                .activeUserCount(activeUserCount != null ? activeUserCount : 0L)
                .build();
    }

    /**
     * 获取用户列表
     *
     * @param userType 用户类型筛选
     * @param status   状态筛选
     * @param keyword  搜索关键词
     * @param page     页码
     * @param size     每页数量
     * @return 用户列表
     */
    public Page<UserListResponse> getUserList(String userType, String status, String keyword, int page, int size) {
        log.info("Getting user list, type: {}, status: {}, keyword: {}", userType, status, keyword);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> userPage = userRepository.findWithFilters(userType, status, keyword, pageable);

        return userPage.map(this::convertToUserListResponse);
    }

    /**
     * 更新用户状态
     *
     * @param userId  用户ID
     * @param request 请求
     */
    @Transactional
    public void updateUserStatus(Long userId, UserStatusUpdateRequest request) {
        log.info("Updating user status: {}, status: {}", userId, request.getStatus());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(UserStatus.valueOf(request.getStatus()));
        userRepository.save(user);

        // TODO: 记录操作日志
    }

    /**
     * 获取待审核认证列表
     *
     * @param status 状态筛选
     * @param page   页码
     * @param size   每页数量
     * @return 认证列表
     */
    public Page<VerificationDetailResponse> getVerificationList(String status, int page, int size) {
        log.info("Getting verification list, status: {}", status);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "createdAt"));

        Page<UserVerification> verificationPage;
        if (status != null && !status.isEmpty()) {
            verificationPage = verificationRepository.findByVerificationStatusOrderByCreatedAtDesc(VerificationStatus.valueOf(status), pageable);
        } else {
            verificationPage = verificationRepository.findAll(pageable);
        }

        return verificationPage.map(this::convertToVerificationResponse);
    }

    /**
     * 获取认证详情
     *
     * @param verificationId 认证ID
     * @return 认证详情
     */
    public VerificationDetailResponse getVerificationDetail(Long verificationId) {
        UserVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_NOT_FOUND));

        return convertToVerificationResponse(verification);
    }

    /**
     * 审核认证
     *
     * @param verificationId 认证ID
     * @param adminId       管理员ID
     * @param request       请求
     * @return 认证详情
     */
    @Transactional
    public VerificationDetailResponse reviewVerification(Long verificationId, Long adminId,
                                                         VerificationReviewRequest request) {
        log.info("Reviewing verification: {}, approved: {}", verificationId, request.isApproved());

        UserVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_NOT_FOUND));

        verification.setVerificationStatus(request.isApproved() ? VerificationStatus.APPROVED : VerificationStatus.REJECTED);
        verification.setAdminNotes(request.getComment());
        verification.setReviewedBy(adminId);
        verification.setReviewedAt(LocalDateTime.now());

        verificationRepository.save(verification);

        // 如果认证通过，更新用户认证状态
        if (request.isApproved()) {
            User user = userRepository.findById(verification.getUser().getId()).orElse(null);
            if (user != null) {
                user.setIsVerified(true);
                userRepository.save(user);
            }
        }

        return convertToVerificationResponse(verification);
    }

    /**
     * 转换为用户列表响应
     */
    private UserListResponse convertToUserListResponse(User user) {
        return UserListResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getRealName())
                .email(user.getEmail())
                .userType(user.getUserType().name())
                .status(user.getStatus().name())
                .isVerified(user.getIsVerified())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * 转换为认证详情响应
     */
    private VerificationDetailResponse convertToVerificationResponse(UserVerification verification) {
        User user = verification.getUser() != null ? verification.getUser() : null;

        return VerificationDetailResponse.builder()
                .id(verification.getId())
                .userId(user != null ? user.getId() : null)
                .userNickname(user != null ? user.getRealName() : null)
                .phone(user != null ? user.getPhone() : null)
                .realName(verification.getRealName())
                .idCardNumber(maskIdCard(verification.getIdCardNumber()))
                .idCardFrontUrl(verification.getIdCardFrontUrl())
                .idCardBackUrl(verification.getIdCardBackUrl())
                .verificationType(verification.getVerificationType())
                .status(verification.getVerificationStatus().name())
                .reviewComment(verification.getAdminNotes())
                .reviewerId(verification.getReviewedBy())
                .reviewedAt(verification.getReviewedAt())
                .createdAt(verification.getCreatedAt())
                .build();
    }

    /**
     * 脱敏身份证号
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "****" + idCard.substring(idCard.length() - 4);
    }
}
