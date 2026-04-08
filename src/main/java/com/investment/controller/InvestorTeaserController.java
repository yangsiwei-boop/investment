package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.investor.TeaserSearchRequest;
import com.investment.dto.response.investor.TeaserDetailResponse;
import com.investment.dto.response.investor.TeaserSearchResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.TeaserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 投资人Teaser搜索控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/investor/teasers")
@RequiredArgsConstructor
@Tag(name = "投资人-Teaser搜索", description = "投资人Teaser搜索相关接口")
@SecurityRequirement(name = "Bearer")
public class InvestorTeaserController {

    private final TeaserSearchService teaserSearchService;

    /**
     * 获取Teaser列表（简单分页）
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return Teaser列表
     */
    @GetMapping
    @Operation(summary = "获取Teaser列表", description = "获取Teaser列表（分页）")
    public ApiResponse<TeaserSearchResponse> getTeasers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting teasers for investor: {}", principal.getId());

        TeaserSearchRequest request = TeaserSearchRequest.builder()
                .page(page)
                .pageSize(size)
                .sortBy("created_at")
                .sortOrder("desc")
                .build();

        Page<TeaserSearchResponse> teaserPage = teaserSearchService.searchTeasers(request, principal.getId());

        TeaserSearchResponse response = TeaserSearchResponse.builder()
                .items(teaserPage.getContent())
                .total(teaserPage.getTotalElements())
                .page(teaserPage.getNumber() + 1)
                .pageSize(teaserPage.getSize())
                .totalPages(teaserPage.getTotalPages())
                .build();

        return ApiResponse.success(response);
    }

    /**
     * 搜索Teaser
     *
     * @param request   搜索请求
     * @param principal 当前用户
     * @return 搜索结果
     */
    @PostMapping("/search")
    @Operation(summary = "搜索Teaser", description = "根据条件搜索Teaser列表")
    public ApiResponse<TeaserSearchResponse> searchTeasers(
            @RequestBody TeaserSearchRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Searching teasers for investor: {}", principal.getId());

        Page<TeaserSearchResponse> page = teaserSearchService.searchTeasers(request, principal.getId());

        TeaserSearchResponse response = TeaserSearchResponse.builder()
                .items(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.success(response);
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
    public ApiResponse<TeaserDetailResponse> getTeaserDetail(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting teaser detail: {}, investor: {}", teaserId, principal.getId());

        TeaserDetailResponse response = teaserSearchService.getTeaserDetail(teaserId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 获取推荐Teaser
     *
     * @param limit     数量限制
     * @param principal 当前用户
     * @return 推荐Teaser列表
     */
    @GetMapping("/recommended")
    @Operation(summary = "获取推荐Teaser", description = "获取为投资人推荐的Teaser列表")
    public ApiResponse<TeaserSearchResponse> getRecommendedTeasers(
            @RequestParam(defaultValue = "10") Integer limit,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting recommended teasers for investor: {}", principal.getId());

        // 简单实现：返回最新的Teaser
        TeaserSearchRequest request = TeaserSearchRequest.builder()
                .page(1)
                .pageSize(limit)
                .sortBy("created_at")
                .sortOrder("desc")
                .build();

        Page<TeaserSearchResponse> page = teaserSearchService.searchTeasers(request, principal.getId());

        TeaserSearchResponse response = TeaserSearchResponse.builder()
                .items(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.success(response);
    }

    /**
     * 获取热门Teaser
     *
     * @param limit     数量限制
     * @param principal 当前用户
     * @return 热门Teaser列表
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门Teaser", description = "获取热门Teaser列表")
    public ApiResponse<TeaserSearchResponse> getHotTeasers(
            @RequestParam(defaultValue = "10") Integer limit,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting hot teasers for investor: {}", principal.getId());

        // 按浏览次数排序
        TeaserSearchRequest request = TeaserSearchRequest.builder()
                .page(1)
                .pageSize(limit)
                .sortBy("view_count")
                .sortOrder("desc")
                .build();

        Page<TeaserSearchResponse> page = teaserSearchService.searchTeasers(request, principal.getId());

        TeaserSearchResponse response = TeaserSearchResponse.builder()
                .items(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.success(response);
    }
}
