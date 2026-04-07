package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.entrepreneur.TeaserCreateRequest;
import com.investment.dto.response.entrepreneur.TeaserResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.EntrepreneurTeaserService;
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
 * 融资用户Teaser控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/entrepreneur/teasers")
@RequiredArgsConstructor
@Tag(name = "融资用户-Teaser管理", description = "融资用户Teaser管理相关接口")
@SecurityRequirement(name = "Bearer")
public class EntrepreneurTeaserController {

    private final EntrepreneurTeaserService teaserService;

    /**
     * 创建Teaser
     *
     * @param request   创建请求
     * @param principal 当前用户
     * @return Teaser信息
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建Teaser", description = "创建新的项目Teaser")
    public ApiResponse<TeaserResponse> createTeaser(
            @Valid @RequestBody TeaserCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Creating teaser for entrepreneur: {}", principal.getId());

        TeaserResponse response = teaserService.createTeaser(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 自动生成Teaser
     *
     * @param projectId 项目ID
     * @param principal 当前用户
     * @return Teaser信息
     */
    @PostMapping("/auto-generate/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "自动生成Teaser", description = "基于项目信息自动生成Teaser")
    public ApiResponse<TeaserResponse> autoGenerateTeaser(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Auto-generating teaser for project: {}, entrepreneur: {}", projectId, principal.getId());

        TeaserResponse response = teaserService.autoGenerateTeaser(principal.getId(), projectId);
        return ApiResponse.success(response);
    }

    /**
     * 获取Teaser列表
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return Teaser列表
     */
    @GetMapping
    @Operation(summary = "获取Teaser列表", description = "获取融资用户的Teaser列表")
    public ApiResponse<Page<TeaserResponse>> getTeaserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting teaser list for entrepreneur: {}", principal.getId());

        Page<TeaserResponse> teasers = teaserService.getTeaserList(principal.getId(), page, size);
        return ApiResponse.success(teasers);
    }

    /**
     * 获取Teaser详情
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return Teaser详情
     */
    @GetMapping("/{teaserId}")
    @Operation(summary = "获取Teaser详情", description = "获取指定Teaser的详细信息")
    public ApiResponse<TeaserResponse> getTeaser(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting teaser: {}, entrepreneur: {}", teaserId, principal.getId());

        TeaserResponse response = teaserService.getTeaser(teaserId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 更新Teaser
     *
     * @param teaserId  Teaser ID
     * @param request   更新请求
     * @param principal 当前用户
     * @return Teaser信息
     */
    @PutMapping("/{teaserId}")
    @Operation(summary = "更新Teaser", description = "更新Teaser信息")
    public ApiResponse<TeaserResponse> updateTeaser(
            @PathVariable Long teaserId,
            @Valid @RequestBody TeaserCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating teaser: {}, entrepreneur: {}", teaserId, principal.getId());

        TeaserResponse response = teaserService.updateTeaser(teaserId, principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 发布Teaser
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return Teaser信息
     */
    @PostMapping("/{teaserId}/publish")
    @Operation(summary = "发布Teaser", description = "发布Teaser使其对投资人可见")
    public ApiResponse<TeaserResponse> publishTeaser(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Publishing teaser: {}, entrepreneur: {}", teaserId, principal.getId());

        TeaserResponse response = teaserService.publishTeaser(teaserId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 下架Teaser
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return Teaser信息
     */
    @PostMapping("/{teaserId}/unpublish")
    @Operation(summary = "下架Teaser", description = "下架Teaser使其对投资人不可见")
    public ApiResponse<TeaserResponse> unpublishTeaser(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Unpublishing teaser: {}, entrepreneur: {}", teaserId, principal.getId());

        TeaserResponse response = teaserService.unpublishTeaser(teaserId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 删除Teaser
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return 成功响应
     */
    @DeleteMapping("/{teaserId}")
    @Operation(summary = "删除Teaser", description = "删除指定Teaser")
    public ApiResponse<Void> deleteTeaser(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Deleting teaser: {}, entrepreneur: {}", teaserId, principal.getId());

        teaserService.deleteTeaser(teaserId, principal.getId());
        return ApiResponse.success(null);
    }
}
