package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.investor.FavoriteRequest;
import com.investment.dto.response.investor.FavoriteResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.InvestorFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投资人收藏控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/investor/favorites")
@RequiredArgsConstructor
@Tag(name = "投资人-收藏管理", description = "投资人收藏相关接口")
@SecurityRequirement(name = "Bearer")
public class InvestorFavoriteController {

    private final InvestorFavoriteService favoriteService;

    /**
     * 添加收藏
     *
     * @param request   收藏请求
     * @param principal 当前用户
     * @return 收藏信息
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "添加收藏", description = "收藏指定Teaser")
    public ApiResponse<FavoriteResponse> addFavorite(
            @RequestBody FavoriteRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Adding favorite for investor: {}, teaser: {}", principal.getId(), request.getTeaserId());

        FavoriteResponse response = favoriteService.addFavorite(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 取消收藏
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return 成功响应
     */
    @DeleteMapping("/{teaserId}")
    @Operation(summary = "取消收藏", description = "取消收藏指定Teaser")
    public ApiResponse<Void> removeFavorite(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Removing favorite for investor: {}, teaser: {}", principal.getId(), teaserId);

        favoriteService.removeFavorite(principal.getId(), teaserId);
        return ApiResponse.success(null);
    }

    /**
     * 获取收藏列表
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 收藏列表
     */
    @GetMapping
    @Operation(summary = "获取收藏列表", description = "获取投资人的收藏列表")
    public ApiResponse<Page<FavoriteResponse>> getFavoriteList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting favorite list for investor: {}", principal.getId());

        Page<FavoriteResponse> favorites = favoriteService.getFavoriteList(principal.getId(), page, size);
        return ApiResponse.success(favorites);
    }

    /**
     * 检查是否已收藏
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return 是否已收藏
     */
    @GetMapping("/check/{teaserId}")
    @Operation(summary = "检查是否已收藏", description = "检查指定Teaser是否已收藏")
    public ApiResponse<Boolean> checkFavorite(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean isFavorited = favoriteService.isFavorited(principal.getId(), teaserId);
        return ApiResponse.success(isFavorited);
    }

    /**
     * 更新收藏分组
     *
     * @param teaserId  Teaser ID
     * @param groupName 分组名
     * @param principal 当前用户
     * @return 成功响应
     */
    @PutMapping("/{teaserId}/group")
    @Operation(summary = "更新收藏分组", description = "更新收藏的分组名称")
    public ApiResponse<Void> updateFavoriteGroup(
            @PathVariable Long teaserId,
            @RequestBody java.util.Map<String, String> body,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating favorite group for investor: {}, teaser: {}", principal.getId(), teaserId);

        favoriteService.updateFavoriteGroup(principal.getId(), teaserId, body.get("groupName"));
        return ApiResponse.success(null);
    }

    /**
     * 获取收藏分组列表
     *
     * @param principal 当前用户
     * @return 分组列表
     */
    @GetMapping("/groups")
    @Operation(summary = "获取收藏分组列表", description = "获取投资人创建的所有收藏分组")
    public ApiResponse<List<String>> getFavoriteGroups(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<String> groups = favoriteService.getFavoriteGroups(principal.getId());
        return ApiResponse.success(groups);
    }
}
