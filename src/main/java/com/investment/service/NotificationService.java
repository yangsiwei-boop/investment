package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.response.common.NotificationResponse;
import com.investment.entity.Notification;
import com.investment.entity.User;
import com.investment.enums.NotificationType;
import com.investment.repository.NotificationRepository;
import com.investment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * 创建通知
     *
     * @param userId  用户ID
     * @param type    通知类型
     * @param title   标题
     * @param content 内容
     * @return 通知
     */
    public Notification createNotification(Long userId, NotificationType type, String title, String content) {
        return createNotification(userId, type, title, content, null, null);
    }

    /**
     * 创建通知（带关联ID）
     *
     * @param userId      用户ID
     * @param type        通知类型
     * @param title       标题
     * @param content     内容
     * @param relatedId   关联ID
     * @param relatedType 关联类型
     * @return 通知
     */
    @Transactional
    public Notification createNotification(Long userId, NotificationType type, String title,
                                           String content, Long relatedId, String relatedType) {
        log.info("Creating notification for user: {}, type: {}", userId, type);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Notification notification = Notification.builder()
                .user(user)
                .notificationType(type)
                .title(title)
                .content(content)
                .relatedId(relatedId)
                .relatedType(relatedType)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    /**
     * 获取用户通知列表
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页数量
     * @return 通知分页列表
     */
    public Page<NotificationResponse> getNotifications(Long userId, int page, int size) {
        log.info("Getting notifications for user: {}", userId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notificationPage = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return notificationPage.map(this::convertToResponse);
    }

    /**
     * 获取未读通知列表
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        log.info("Getting unread notifications for user: {}", userId);

        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    /**
     * 获取未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param userId         用户ID
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        log.info("Marking notification as read: {}, user: {}", notificationId, userId);

        Notification notification = findByIdAndUserId(notificationId, userId);

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    /**
     * 标记所有通知为已读
     *
     * @param userId 用户ID
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);

        notificationRepository.markAllAsReadByUserId(userId);
    }

    /**
     * 删除通知
     *
     * @param notificationId 通知ID
     * @param userId         用户ID
     */
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        log.info("Deleting notification: {}, user: {}", notificationId, userId);

        Notification notification = findByIdAndUserId(notificationId, userId);

        notificationRepository.delete(notification);
    }

    /**
     * 清空所有通知
     *
     * @param userId 用户ID
     */
    @Transactional
    public void clearAllNotifications(Long userId) {
        log.info("Clearing all notifications for user: {}", userId);

        notificationRepository.deleteByUserId(userId);
    }

    /**
     * 根据ID和用户ID查找通知
     */
    private Notification findByIdAndUserId(Long notificationId, Long userId) {
        return notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    /**
     * 转换为响应DTO
     */
    private NotificationResponse convertToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getNotificationType().name())
                .title(notification.getTitle())
                .content(notification.getContent())
                .relatedId(notification.getRelatedId())
                .relatedType(notification.getRelatedType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
