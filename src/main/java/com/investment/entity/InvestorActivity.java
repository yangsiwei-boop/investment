package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 投资人活动实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investor_activities", indexes = {
        @Index(name = "idx_investor_user_id", columnList = "investor_user_id"),
        @Index(name = "idx_teaser_id", columnList = "teaser_id"),
        @Index(name = "idx_activity_type", columnList = "activity_type")
})
public class InvestorActivity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 投资人用户ID
     */
    @Column(name = "investor_user_id", nullable = false)
    private Long investorUserId;

    /**
     * Teaser ID
     */
    @Column(name = "teaser_id")
    private Long teaserId;

    /**
     * 活动类型 (VIEW, FAVORITE, ANALYZE, APPLY等)
     */
    @Column(name = "activity_type", length = 20, nullable = false)
    private String activityType;

    /**
     * 分组名称（用于收藏分组等）
     */
    @Column(name = "group_name", length = 100)
    private String groupName;

    /**
     * 活动备注
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
