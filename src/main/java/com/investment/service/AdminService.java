package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.admin.RoleCreateRequest;
import com.investment.dto.request.admin.RoleUpdateRequest;
import com.investment.dto.request.admin.UserStatusUpdateRequest;
import com.investment.dto.request.admin.VerificationReviewRequest;
import com.investment.dto.response.admin.*;
import com.investment.entity.*;
import com.investment.enums.FinancingStage;
import com.investment.enums.IndustryType;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final FavoriteRepository favoriteRepository;
    private final ViewHistoryRepository viewHistoryRepository;

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

    /**
     * 获取权限列表
     *
     * @return 权限列表
     */
    public List<PermissionResponse> getPermissionList() {
        log.info("Getting permission list");

        List<Permission> permissions = permissionRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
        return permissions.stream()
                .map(this::convertToPermissionResponse)
                .toList();
    }

    /**
     * 获取角色列表（含权限信息）
     *
     * @return 角色列表
     */
    public List<RoleResponse> getRoleList() {
        log.info("Getting role list with permissions");

        List<Role> roles = roleRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
        List<RoleResponse> result = new ArrayList<>();

        for (Role role : roles) {
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());
            List<PermissionResponse> permResponses = rolePermissions.stream()
                    .map(rp -> convertToPermissionResponse(rp.getPermission()))
                    .toList();

            result.add(RoleResponse.builder()
                    .id(role.getId())
                    .roleCode(role.getRoleCode())
                    .roleName(role.getRoleName())
                    .description(role.getDescription())
                    .roleLevel(role.getRoleLevel())
                    .isSystem(role.getIsSystem())
                    .isEnabled(role.getIsEnabled())
                    .sortOrder(role.getSortOrder())
                    .permissionsCount(permResponses.size())
                    .usersCount(role.getUsersCount())
                    .permissions(permResponses)
                    .createdAt(role.getCreatedAt())
                    .updatedAt(role.getUpdatedAt())
                    .build());
        }

        return result;
    }

    /**
     * 创建角色
     *
     * @param request 创建请求
     * @return 创建后的角色
     */
    @Transactional
    public RoleResponse createRole(RoleCreateRequest request) {
        log.info("Creating role: {}", request.getRoleCode());

        // 检查角色编码是否已存在
        if (roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "角色编码已存在: " + request.getRoleCode());
        }

        Role role = Role.builder()
                .roleCode(request.getRoleCode())
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .roleLevel(request.getRoleLevel() != null ? request.getRoleLevel() : 0)
                .isSystem(false)
                .isEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .permissionsCount(0)
                .usersCount(0)
                .build();

        roleRepository.save(role);

        // 关联权限
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            for (Long permId : request.getPermissionIds()) {
                Permission perm = permissionRepository.findById(permId).orElse(null);
                if (perm != null) {
                    RolePermission rp = RolePermission.builder()
                            .role(role)
                            .permission(perm)
                            .build();
                    rolePermissionRepository.save(rp);
                }
            }
            role.setPermissionsCount(request.getPermissionIds().size());
            roleRepository.save(role);
        }

        // 返回创建后的角色
        return getRoleList().stream()
                .filter(r -> r.getId().equals(role.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR, "创建角色失败"));
    }

    /**
     * 更新角色
     *
     * @param roleId  角色ID
     * @param request 更新请求
     * @return 更新后的角色
     */
    @Transactional
    public RoleResponse updateRole(Long roleId, RoleUpdateRequest request) {
        log.info("Updating role: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PERMISSION, "角色不存在"));

        if (request.getRoleName() != null) {
            role.setRoleName(request.getRoleName());
        }
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        if (request.getRoleLevel() != null) {
            role.setRoleLevel(request.getRoleLevel());
        }
        if (request.getIsEnabled() != null) {
            role.setIsEnabled(request.getIsEnabled());
        }
        if (request.getSortOrder() != null) {
            role.setSortOrder(request.getSortOrder());
        }

        // 更新权限关联
        if (request.getPermissionIds() != null) {
            rolePermissionRepository.deleteByRoleId(roleId);
            for (Long permId : request.getPermissionIds()) {
                Permission perm = permissionRepository.findById(permId).orElse(null);
                if (perm != null) {
                    RolePermission rp = RolePermission.builder()
                            .role(role)
                            .permission(perm)
                            .build();
                    rolePermissionRepository.save(rp);
                }
            }
            role.setPermissionsCount(request.getPermissionIds().size());
        }

        roleRepository.save(role);

        // 返回更新后的角色
        return getRoleList().stream()
                .filter(r -> r.getId().equals(roleId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PERMISSION, "角色不存在"));
    }

    /**
     * 获取数据统计
     *
     * @return 统计数据
     */
    public StatisticsResponse getStatistics() {
        log.info("Getting admin statistics");

        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime weekStart = today.minusDays(7).atStartOfDay();
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();

        // 概览统计
        StatisticsResponse.OverviewStats overview = StatisticsResponse.OverviewStats.builder()
                .totalUsers(userRepository.count())
                .newUsersToday(userRepository.countByCreatedAtAfter(todayStart))
                .newUsersThisWeek(userRepository.countByCreatedAtAfter(weekStart))
                .newUsersThisMonth(userRepository.countByCreatedAtAfter(monthStart))
                .totalProjects(projectRepository.count())
                .newProjectsToday(projectRepository.countByCreatedAtAfter(todayStart))
                .totalTeasers(teaserRepository.count())
                .publishedTeasers(teaserRepository.countByStatus(com.investment.enums.TeaserStatus.PUBLISHED))
                .totalViews(safeLong(teaserRepository.sumAllViewCount()))
                .totalFavorites(favoriteRepository.count())
                .totalApplications(applicationRepository.count())
                .pendingApplications(applicationRepository.countByApplicationStatus(
                        com.investment.enums.ApplicationStatus.PENDING))
                .build();

        // 用户增长趋势（近7天）
        List<StatisticsResponse.TrendItem> userTrend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
            Long count = userRepository.countByCreatedAtBetween(dayStart, dayEnd);
            userTrend.add(StatisticsResponse.TrendItem.builder()
                    .date(date.format(DateTimeFormatter.ISO_DATE))
                    .value(count != null ? count : 0L)
                    .build());
        }

        // 项目趋势（近7天）
        List<StatisticsResponse.TrendItem> projectTrend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
            Long count = projectRepository.countByCreatedAtBetween(dayStart, dayEnd);
            projectTrend.add(StatisticsResponse.TrendItem.builder()
                    .date(date.format(DateTimeFormatter.ISO_DATE))
                    .value(count != null ? count : 0L)
                    .build());
        }

        // 浏览趋势（近7天）
        List<StatisticsResponse.TrendItem> viewTrend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
            Long count = viewHistoryRepository.countByCreatedAtBetween(dayStart, dayEnd);
            viewTrend.add(StatisticsResponse.TrendItem.builder()
                    .date(date.format(DateTimeFormatter.ISO_DATE))
                    .value(count != null ? count : 0L)
                    .build());
        }

        // 行业分布
        List<StatisticsResponse.DistributionItem> industryDistribution = new ArrayList<>();
        for (IndustryType industry : IndustryType.values()) {
            Long count = projectRepository.countByIndustry(industry);
            if (count != null && count > 0) {
                industryDistribution.add(StatisticsResponse.DistributionItem.builder()
                        .name(industry.getDescription())
                        .value(count)
                        .build());
            }
        }

        // 融资阶段分布
        List<StatisticsResponse.DistributionItem> stageDistribution = new ArrayList<>();
        for (FinancingStage stage : FinancingStage.values()) {
            Long count = projectRepository.countByFinancingStage(stage);
            if (count != null && count > 0) {
                stageDistribution.add(StatisticsResponse.DistributionItem.builder()
                        .name(stage.getDescription())
                        .value(count)
                        .build());
            }
        }

        return StatisticsResponse.builder()
                .overview(overview)
                .userTrend(userTrend)
                .projectTrend(projectTrend)
                .viewTrend(viewTrend)
                .industryDistribution(industryDistribution)
                .stageDistribution(stageDistribution)
                .build();
    }

    private PermissionResponse convertToPermissionResponse(Permission p) {
        return PermissionResponse.builder()
                .id(p.getId())
                .permissionCode(p.getPermissionCode())
                .permissionName(p.getPermissionName())
                .description(p.getDescription())
                .module(p.getModule())
                .parentId(p.getParentId())
                .permissionType(p.getPermissionType())
                .resourcePath(p.getResourcePath())
                .httpMethods(p.getHttpMethods())
                .sortOrder(p.getSortOrder())
                .isEnabled(p.getIsEnabled())
                .icon(p.getIcon())
                .build();
    }

    private Long safeLong(Long value) {
        return value != null ? value : 0L;
    }
}
