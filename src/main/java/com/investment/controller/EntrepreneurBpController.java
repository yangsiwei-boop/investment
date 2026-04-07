package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.entrepreneur.BusinessPlanResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.BusinessPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 融资用户商业计划书控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/entrepreneur/bp")
@RequiredArgsConstructor
@Tag(name = "融资用户-商业计划书", description = "融资用户商业计划书相关接口")
@SecurityRequirement(name = "Bearer")
public class EntrepreneurBpController {

    private final BusinessPlanService businessPlanService;

    /**
     * 上传商业计划书
     *
     * @param projectId 项目ID
     * @param file      文件
     * @param note      备注
     * @param principal 当前用户
     * @return BP信息
     */
    @PostMapping("/upload/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "上传商业计划书", description = "为项目上传商业计划书")
    public ApiResponse<BusinessPlanResponse> uploadBp(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "note", required = false) String note,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Uploading BP for project: {}, entrepreneur: {}", projectId, principal.getId());

        BusinessPlanResponse response = businessPlanService.uploadBusinessPlan(
                projectId, principal.getId(), file);
        return ApiResponse.success(response);
    }

    /**
     * 获取项目的BP列表
     *
     * @param projectId 项目ID
     * @param principal 当前用户
     * @return BP列表
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "获取项目BP列表", description = "获取指定项目的所有商业计划书")
    public ApiResponse<List<BusinessPlanResponse>> getBpList(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting BP list for project: {}, entrepreneur: {}", projectId, principal.getId());

        List<BusinessPlanResponse> response = businessPlanService.getBusinessPlanList(projectId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 获取BP详情
     *
     * @param bpId      BP ID
     * @param principal 当前用户
     * @return BP详情
     */
    @GetMapping("/{bpId}")
    @Operation(summary = "获取BP详情", description = "获取指定商业计划书的详细信息")
    public ApiResponse<BusinessPlanResponse> getBp(
            @PathVariable Long bpId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting BP: {}, entrepreneur: {}", bpId, principal.getId());

        BusinessPlanResponse response = businessPlanService.getBusinessPlan(bpId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 删除BP
     *
     * @param bpId      BP ID
     * @param principal 当前用户
     * @return 成功响应
     */
    @DeleteMapping("/{bpId}")
    @Operation(summary = "删除BP", description = "删除指定的商业计划书")
    public ApiResponse<Void> deleteBp(
            @PathVariable Long bpId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Deleting BP: {}, entrepreneur: {}", bpId, principal.getId());

        businessPlanService.deleteBusinessPlan(bpId, principal.getId());
        return ApiResponse.success(null);
    }
}
