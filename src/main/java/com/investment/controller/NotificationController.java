package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.common.NotificationResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "通用-通知管理", description = "通知管理相关接口")
@SecurityRequirement(name = "Bearer")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取通知列表
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 通知列表
     */
    @GetMapping
    @Operation(summary = "获取通知列表", description = "获取用户的通知列表")
    public ApiResponse<Page<NotificationResponse>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting notifications for user: {}", principal.getId());

        Page<NotificationResponse> notifications = notificationService.getNotifications(principal.getId(), page, size);
        return ApiResponse.success(notifications);
    }

    /**
     * 获取未读通知
     *
     * @param principal 当前用户
     * @return 未读通知列表
     */
    @GetMapping("/unread")
    @Operation(summary = "获取未读通知", description = "获取用户的未读通知列表")
    public ApiResponse<List<NotificationResponse>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting unread notifications for user: {}", principal.getId());

        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(principal.getId());
        return ApiResponse.success(notifications);
    }

    /**
     * 获取未读通知数量
     *
     * @param principal 当前用户
     * @return 未读数量
     */
    @GetMapping("/unread/count")
    @Operation(summary = "获取未读通知数量", description = "获取用户的未读通知数量")
    public ApiResponse<Long> getUnreadCount(@AuthenticationPrincipal UserPrincipal principal) {
        long count = notificationService.getUnreadCount(principal.getId());
        return ApiResponse.success(count);
    }

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param principal      当前用户
     * @return 成功响应
     */
    @PutMapping("/{notificationId}/read")
    @Operation(summary = "标记通知为已读", description = "将指定通知标记为已读")
    public ApiResponse<Void> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Marking notification as read: {}, user: {}", notificationId, principal.getId());

        notificationService.markAsRead(notificationId, principal.getId());
        return ApiResponse.success(null);
    }

    /**
     * 标记所有通知为已读
     *
     * @param principal 当前用户
     * @return 成功响应
     */
    @PutMapping("/read-all")
    @Operation(summary = "标记所有通知为已读", description = "将所有通知标记为已读")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal UserPrincipal principal) {
        log.info("Marking all notifications as read for user: {}", principal.getId());

        notificationService.markAllAsRead(principal.getId());
        return ApiResponse.success(null);
    }

    /**
     * 删除通知
     *
     * @param notificationId 通知ID
     * @param principal      当前用户
     * @return 成功响应
     */
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "删除通知", description = "删除指定通知")
    public ApiResponse<Void> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Deleting notification: {}, user: {}", notificationId, principal.getId());

        notificationService.deleteNotification(notificationId, principal.getId());
        return ApiResponse.success(null);
    }

    /**
     * 清空所有通知
     *
     * @param principal 当前用户
     * @return 成功响应
     */
    @DeleteMapping("/clear")
    @Operation(summary = "清空所有通知", description = "清空用户的所有通知")
    public ApiResponse<Void> clearAllNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        log.info("Clearing all notifications for user: {}", principal.getId());

        notificationService.clearAllNotifications(principal.getId());
        return ApiResponse.success(null);
    }
}
