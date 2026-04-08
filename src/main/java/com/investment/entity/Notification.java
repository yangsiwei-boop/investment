package com.investment.entity;

import com.investment.enums.NotificationType;
import com.investment.enums.NotificationTypeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 通知实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 接收用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 通知类型
     */
    @Convert(converter = NotificationTypeConverter.class)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType;

    /**
     * 通知标题
     */
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    /**
     * 通知内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 关联ID(如项目ID、申请ID等)
     */
    @Column(name = "related_id")
    private Long relatedId;

    /**
     * 关联类型
     */
    @Column(name = "related_type", length = 50)
    private String relatedType;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    /**
     * 覆盖父类的updatedAt字段，数据库表中不存在此列
     */
    @Transient
    private LocalDateTime updatedAt;
}
