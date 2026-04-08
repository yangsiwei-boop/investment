package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 收藏实体
 * 对应数据库 favorites 表
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favorites", indexes = {
        @Index(name = "idx_favorites_user_id", columnList = "user_id"),
        @Index(name = "idx_favorites_teaser_id", columnList = "teaser_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_teaser", columnNames = {"user_id", "teaser_id"})
})
public class Favorite {

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
    @Column(name = "teaser_id", nullable = false)
    private Long teaserId;

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
