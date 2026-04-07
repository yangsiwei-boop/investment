package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.entrepreneur.EntrepreneurDashboardResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.EntrepreneurDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 融资用户工作台控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/entrepreneur/dashboard")
@RequiredArgsConstructor
@Tag(name = "融资用户-工作台", description = "融资用户工作台相关接口")
@SecurityRequirement(name = "Bearer")
public class EntrepreneurDashboardController {

    private final EntrepreneurDashboardService dashboardService;

    /**
     * 获取工作台首页数据
     *
     * @param principal 当前用户
     * @return 工作台数据
     */
    @GetMapping
    @Operation(summary = "获取工作台首页数据", description = "获取融资用户工作台首页统计数据")
    public ApiResponse<EntrepreneurDashboardResponse> getDashboard(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting dashboard for entrepreneur: {}", principal.getId());

        EntrepreneurDashboardResponse response = dashboardService.getDashboard(principal.getId());
        return ApiResponse.success(response);
    }
}
