package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 浏览记录实体
 * 对应数据库 view_histories 表
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "view_histories", indexes = {
        @Index(name = "idx_view_histories_user_id", columnList = "user_id"),
        @Index(name = "idx_view_histories_teaser_id", columnList = "teaser_id"),
        @Index(name = "idx_view_histories_created_at", columnList = "created_at")
})
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Teaser ID
     */
    @Column(name = "teaser_id")
    private Long teaserId;

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 浏览时长(秒)
     */
    @Column(name = "view_duration")
    private Integer viewDuration;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
