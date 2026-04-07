package com.investment.repository;

import com.investment.entity.Notification;
import com.investment.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通知数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 根据用户ID查找通知列表
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 通知分页列表
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    Page<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    /**
     * 根据用户ID和是否已读查找通知
     *
     * @param userId 用户ID
     * @param isRead 是否已读
     * @param pageable 分页参数
     * @return 通知分页列表
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = :isRead ORDER BY n.createdAt DESC")
    Page<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(
            @Param("userId") Long userId,
            @Param("isRead") Boolean isRead,
            Pageable pageable);

    /**
     * 根据用户ID查找未读通知
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(@Param("userId") Long userId);

    /**
     * 根据用户ID和通知类型查找通知
     *
     * @param userId           用户ID
     * @param notificationType 通知类型
     * @param pageable         分页参数
     * @return 通知分页列表
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.notificationType = :notificationType ORDER BY n.createdAt DESC")
    Page<Notification> findByUserIdAndNotificationTypeOrderByCreatedAtDesc(
            @Param("userId") Long userId,
            @Param("notificationType") NotificationType notificationType,
            Pageable pageable);

    /**
     * 统计用户未读通知数量
     *
     * @param userId 用户ID
     * @return 数量
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    Long countByUserIdAndIsReadFalse(@Param("userId") Long userId);

    /**
     * 根据ID和用户ID查找通知
     *
     * @param id     通知ID
     * @param userId 用户ID
     * @return 通知
     */
    @Query("SELECT n FROM Notification n WHERE n.id = :id AND n.user.id = :userId")
    Optional<Notification> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 标记用户所有通知为已读
     *
     * @param userId 用户ID
     * @return 更新的记录数
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    int markAllAsReadByUserId(@Param("userId") Long userId);

    /**
     * 批量标记通知为已读
     *
     * @param notificationIds 通知ID列表
     * @return 更新的记录数
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id IN :notificationIds")
    int markAsReadByIds(@Param("notificationIds") List<Long> notificationIds);

    /**
     * 删除用户的所有已读通知
     *
     * @param userId 用户ID
     * @return 删除的记录数
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId AND n.isRead = true")
    int deleteReadNotificationsByUserId(@Param("userId") Long userId);

    /**
     * 删除用户的所有通知
     *
     * @param userId 用户ID
     * @return 删除的记录数
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
