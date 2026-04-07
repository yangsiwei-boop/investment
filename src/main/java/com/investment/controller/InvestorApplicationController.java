package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.application.ApplicationCreateRequest;
import com.investment.dto.response.application.ApplicationResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 投资人申请控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/investor/applications")
@RequiredArgsConstructor
@Tag(name = "投资人-申请管理", description = "投资人申请相关接口")
@SecurityRequirement(name = "Bearer")
public class InvestorApplicationController {

    private final ApplicationService applicationService;

    /**
     * 创建申请
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 申请信息
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建申请", description = "创建查看BP或获取联系方式的申请")
    public ApiResponse<ApplicationResponse> createApplication(
            @Valid @RequestBody ApplicationCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Creating application for investor: {}", principal.getId());

        ApplicationResponse response = applicationService.createApplication(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取我的申请列表
     *
     * @param status    状态筛选
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 申请列表
     */
    @GetMapping
    @Operation(summary = "获取我的申请列表", description = "获取投资人提交的所有申请")
    public ApiResponse<Page<ApplicationResponse>> getMyApplications(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting applications for investor: {}", principal.getId());

        Page<ApplicationResponse> applications = applicationService.getInvestorApplications(
                principal.getId(), status, page, size);
        return ApiResponse.success(applications);
    }

    /**
     * 获取申请详情
     *
     * @param applicationId 申请ID
     * @param principal     当前用户
     * @return 申请详情
     */
    @GetMapping("/{applicationId}")
    @Operation(summary = "获取申请详情", description = "获取指定申请的详细信息")
    public ApiResponse<ApplicationResponse> getApplication(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting application: {}, investor: {}", applicationId, principal.getId());

        ApplicationResponse response = applicationService.getApplication(applicationId, principal.getId());
        return ApiResponse.success(response);
    }
}
