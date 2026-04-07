package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.profile.*;
import com.investment.dto.response.profile.*;
import com.investment.entity.PrivacySetting;
import com.investment.security.UserPrincipal;
import com.investment.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 用户资料控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(name = "用户-资料管理", description = "用户资料管理相关接口")
@SecurityRequirement(name = "Bearer")
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * 获取用户基本信息
     *
     * @param principal 当前用户
     * @return 用户信息
     */
    @GetMapping
    @Operation(summary = "获取用户基本信息", description = "获取当前用户的基本信息")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting user profile: {}", principal.getId());

        UserProfileResponse response = userProfileService.getUserProfile(principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 更新用户基本信息
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 用户信息
     */
    @PutMapping
    @Operation(summary = "更新用户基本信息", description = "更新当前用户的基本信息")
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @Valid @RequestBody UserProfileUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating user profile: {}", principal.getId());

        UserProfileResponse response = userProfileService.updateUserProfile(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取投资人资料
     *
     * @param principal 当前用户
     * @return 投资人资料
     */
    @GetMapping("/investor")
    @Operation(summary = "获取投资人资料", description = "获取投资人详细资料")
    public ApiResponse<InvestorProfileResponse> getInvestorProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting investor profile: {}", principal.getId());

        InvestorProfileResponse response = userProfileService.getInvestorProfile(principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 更新投资人资料
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 投资人资料
     */
    @PutMapping("/investor")
    @Operation(summary = "更新投资人资料", description = "更新投资人详细资料")
    public ApiResponse<InvestorProfileResponse> updateInvestorProfile(
            @Valid @RequestBody InvestorProfileUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating investor profile: {}", principal.getId());

        InvestorProfileResponse response = userProfileService.updateInvestorProfile(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取融资用户资料
     *
     * @param principal 当前用户
     * @return 融资用户资料
     */
    @GetMapping("/entrepreneur")
    @Operation(summary = "获取融资用户资料", description = "获取融资用户详细资料")
    public ApiResponse<EntrepreneurProfileResponse> getEntrepreneurProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting entrepreneur profile: {}", principal.getId());

        EntrepreneurProfileResponse response = userProfileService.getEntrepreneurProfile(principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 更新融资用户资料
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 融资用户资料
     */
    @PutMapping("/entrepreneur")
    @Operation(summary = "更新融资用户资料", description = "更新融资用户详细资料")
    public ApiResponse<EntrepreneurProfileResponse> updateEntrepreneurProfile(
            @Valid @RequestBody EntrepreneurProfileUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating entrepreneur profile: {}", principal.getId());

        EntrepreneurProfileResponse response = userProfileService.updateEntrepreneurProfile(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 提交实名认证申请
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 认证ID
     */
    @PostMapping("/verification")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "提交实名认证", description = "提交实名认证申请")
    public ApiResponse<Long> submitVerification(
            @Valid @RequestBody VerificationRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Submitting verification for user: {}", principal.getId());

        Long verificationId = userProfileService.submitVerification(principal.getId(), request);
        return ApiResponse.success(verificationId);
    }

    /**
     * 获取认证状态
     *
     * @param principal 当前用户
     * @return 认证状态
     */
    @GetMapping("/verification/status")
    @Operation(summary = "获取认证状态", description = "获取当前用户的认证状态")
    public ApiResponse<String> getVerificationStatus(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting verification status for user: {}", principal.getId());

        String status = userProfileService.getVerificationStatus(principal.getId());
        return ApiResponse.success(status);
    }

    /**
     * 获取隐私设置
     *
     * @param principal 当前用户
     * @return 隐私设置
     */
    @GetMapping("/privacy")
    @Operation(summary = "获取隐私设置", description = "获取当前用户的隐私设置")
    public ApiResponse<PrivacySetting> getPrivacySetting(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting privacy setting for user: {}", principal.getId());

        PrivacySetting setting = userProfileService.getPrivacySetting(principal.getId());
        return ApiResponse.success(setting);
    }

    /**
     * 更新隐私设置
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 隐私设置
     */
    @PutMapping("/privacy")
    @Operation(summary = "更新隐私设置", description = "更新当前用户的隐私设置")
    public ApiResponse<PrivacySetting> updatePrivacySetting(
            @RequestBody PrivacySettingUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating privacy setting for user: {}", principal.getId());

        PrivacySetting setting = userProfileService.updatePrivacySetting(principal.getId(), request);
        return ApiResponse.success(setting);
    }
}
