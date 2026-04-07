package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.application.ApplicationResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 融资用户申请管理控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/entrepreneur/applications")
@RequiredArgsConstructor
@Tag(name = "融资用户-申请管理", description = "融资用户申请审核相关接口")
@SecurityRequirement(name = "Bearer")
public class EntrepreneurApplicationController {

    private final ApplicationService applicationService;

    /**
     * 获取收到的申请列表
     *
     * @param status    状态筛选
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 申请列表
     */
    @GetMapping
    @Operation(summary = "获取收到的申请列表", description = "获取融资用户收到的所有申请")
    public ApiResponse<Page<ApplicationResponse>> getApplications(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting applications for entrepreneur: {}", principal.getId());

        Page<ApplicationResponse> applications = applicationService.getEntrepreneurApplications(
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
        log.info("Getting application: {}, entrepreneur: {}", applicationId, principal.getId());

        ApplicationResponse response = applicationService.getApplication(applicationId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 审核申请
     *
     * @param applicationId 申请ID
     * @param approved      是否批准
     * @param comment       审核意见
     * @param principal     当前用户
     * @return 申请信息
     */
    @PostMapping("/{applicationId}/review")
    @Operation(summary = "审核申请", description = "审核投资人的申请（批准或拒绝）")
    public ApiResponse<ApplicationResponse> reviewApplication(
            @PathVariable Long applicationId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String comment,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Reviewing application: {}, entrepreneur: {}, approved: {}", applicationId, principal.getId(), approved);

        ApplicationResponse response = applicationService.reviewApplication(
                applicationId, principal.getId(), approved, comment);
        return ApiResponse.success(response);
    }
}
