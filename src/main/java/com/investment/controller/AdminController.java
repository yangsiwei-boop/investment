package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.admin.ApplicationReviewRequest;
import com.investment.dto.request.admin.RoleCreateRequest;
import com.investment.dto.request.admin.UserStatusUpdateRequest;
import com.investment.dto.request.admin.VerificationReviewRequest;
import com.investment.dto.request.admin.RoleUpdateRequest;
import com.investment.dto.response.admin.*;
import com.investment.security.UserPrincipal;
import com.investment.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "后台管理", description = "后台管理相关接口")
@SecurityRequirement(name = "Bearer")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    /**
     * 获取仪表盘数据
     *
     * @return 仪表盘数据
     */
    @GetMapping("/dashboard")
    @Operation(summary = "获取仪表盘数据", description = "获取后台管理仪表盘统计数据")
    public ApiResponse<AdminDashboardResponse> getDashboard() {
        log.info("Getting admin dashboard");

        AdminDashboardResponse response = adminService.getDashboard();
        return ApiResponse.success(response);
    }

    /**
     * 获取用户列表
     *
     * @param userType 用户类型
     * @param status   状态
     * @param keyword  关键词
     * @param page     页码
     * @param size     每页数量
     * @return 用户列表
     */
    @GetMapping("/users")
    @Operation(summary = "获取用户列表", description = "获取系统用户列表（支持筛选和搜索）")
    public ApiResponse<Page<UserListResponse>> getUserList(
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting user list");

        Page<UserListResponse> users = adminService.getUserList(userType, status, keyword, page, size);
        return ApiResponse.success(users);
    }

    /**
     * 更新用户状态
     *
     * @param userId   用户ID
     * @param request  请求
     * @return 成功响应
     */
    @PutMapping("/users/{userId}/status")
    @Operation(summary = "更新用户状态", description = "更新指定用户的状态（启用/禁用/封号）")
    public ApiResponse<Void> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UserStatusUpdateRequest request) {
        log.info("Updating user status: {}", userId);

        adminService.updateUserStatus(userId, request);
        return ApiResponse.success(null);
    }

    /**
     * 获取待审核认证列表
     *
     * @param status 状态
     * @param page   页码
     * @param size   每页数量
     * @return 认证列表
     */
    @GetMapping("/verifications")
    @Operation(summary = "获取认证列表", description = "获取实名认证申请列表")
    public ApiResponse<Page<VerificationDetailResponse>> getVerificationList(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting verification list");

        Page<VerificationDetailResponse> verifications = adminService.getVerificationList(status, page, size);
        return ApiResponse.success(verifications);
    }

    /**
     * 获取认证详情
     *
     * @param verificationId 认证ID
     * @return 认证详情
     */
    @GetMapping("/verifications/{verificationId}")
    @Operation(summary = "获取认证详情", description = "获取指定认证申请的详细信息")
    public ApiResponse<VerificationDetailResponse> getVerificationDetail(
            @PathVariable Long verificationId) {
        log.info("Getting verification detail: {}", verificationId);

        VerificationDetailResponse response = adminService.getVerificationDetail(verificationId);
        return ApiResponse.success(response);
    }

    /**
     * 审核认证
     *
     * @param verificationId 认证ID
     * @param request       请求
     * @param principal     当前用户
     * @return 认证详情
     */
    @PostMapping("/verifications/{verificationId}/review")
    @Operation(summary = "审核认证", description = "审核实名认证申请")
    public ApiResponse<VerificationDetailResponse> reviewVerification(
            @PathVariable Long verificationId,
            @Valid @RequestBody VerificationReviewRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Reviewing verification: {}, admin: {}", verificationId, principal.getId());

        VerificationDetailResponse response = adminService.reviewVerification(
                verificationId, principal.getId(), request);
        return ApiResponse.success(response);
    }

    // ============================================
    // BP申请审核管理
    // ============================================

    /**
     * 获取申请列表
     *
     * @param status 状态筛选
     * @param page   页码
     * @param size   每页数量
     * @return 申请列表
     */
    @GetMapping("/applications")
    @Operation(summary = "获取申请列表", description = "获取BP申请/联系企业申请列表")
    public ApiResponse<Page<ApplicationDetailResponse>> getApplicationList(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting application list, status: {}", status);

        Page<ApplicationDetailResponse> applications = adminService.getApplicationList(status, page, size);
        return ApiResponse.success(applications);
    }

    /**
     * 获取申请详情
     *
     * @param applicationId 申请ID
     * @return 申请详情
     */
    @GetMapping("/applications/{applicationId}")
    @Operation(summary = "获取申请详情", description = "获取指定BP申请的详细信息")
    public ApiResponse<ApplicationDetailResponse> getApplicationDetail(
            @PathVariable Long applicationId) {
        log.info("Getting application detail: {}", applicationId);

        ApplicationDetailResponse response = adminService.getApplicationDetail(applicationId);
        return ApiResponse.success(response);
    }

    /**
     * 审核申请
     *
     * @param applicationId 申请ID
     * @param request       审核请求
     * @param principal     当前用户
     * @return 申请详情
     */
    @PostMapping("/applications/{applicationId}/review")
    @Operation(summary = "审核申请", description = "审核BP申请或联系企业申请")
    public ApiResponse<ApplicationDetailResponse> reviewApplication(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationReviewRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Reviewing application: {}, admin: {}", applicationId, principal.getId());

        ApplicationDetailResponse response = adminService.reviewApplication(
                applicationId, principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取权限列表
     *
     * @return 权限列表
     */
    @GetMapping("/permissions")
    @Operation(summary = "获取权限列表", description = "获取系统所有可用权限列表")
    public ApiResponse<List<PermissionResponse>> getPermissionList() {
        log.info("Getting permission list");

        List<PermissionResponse> permissions = adminService.getPermissionList();
        return ApiResponse.success(permissions);
    }

    /**
     * 获取角色列表（含权限信息）
     *
     * @return 角色列表
     */
    @GetMapping("/roles")
    @Operation(summary = "获取角色列表", description = "获取系统角色列表及其关联权限")
    public ApiResponse<List<RoleResponse>> getRoleList() {
        log.info("Getting role list");

        List<RoleResponse> roles = adminService.getRoleList();
        return ApiResponse.success(roles);
    }

    /**
     * 创建角色
     *
     * @param request 创建请求
     * @return 创建后的角色
     */
    @PostMapping("/roles")
    @Operation(summary = "创建角色", description = "创建新的系统角色")
    public ApiResponse<RoleResponse> createRole(
            @Valid @RequestBody RoleCreateRequest request) {
        log.info("Creating role: {}", request.getRoleCode());

        RoleResponse response = adminService.createRole(request);
        return ApiResponse.success(response);
    }

    /**
     * 更新角色
     *
     * @param roleId  角色ID
     * @param request 更新请求
     * @return 更新后的角色
     */
    @PutMapping("/roles/{roleId}")
    @Operation(summary = "更新角色", description = "更新角色信息")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable Long roleId,
            @RequestBody RoleUpdateRequest request) {
        log.info("Updating role: {}", roleId);

        RoleResponse response = adminService.updateRole(roleId, request);
        return ApiResponse.success(response);
    }

    /**
     * 获取数据统计
     *
     * @param period 统计周期：today(今天)、7d(近7天)、30d(近30天)，默认7d
     * @return 统计数据
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取数据统计", description = "获取系统统计数据（概览、趋势、分布），支持按周期查询")
    public ApiResponse<StatisticsResponse> getStatistics(
            @RequestParam(defaultValue = "7d") String period) {
        log.info("Getting admin statistics, period: {}", period);

        StatisticsResponse response = adminService.getStatistics(period);
        return ApiResponse.success(response);
    }
}
