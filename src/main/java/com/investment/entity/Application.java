package com.investment.entity;

import com.investment.enums.ApplicationStatus;
import com.investment.enums.ApplicationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 申请实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 申请类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_type", nullable = false, length = 20)
    private ApplicationType applicationType;

    /**
     * 投资人用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_user_id", nullable = false)
    private User investorUser;

    /**
     * 项目ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    /**
     * 融资用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepreneur_user_id", nullable = false)
    private User entrepreneurUser;

    /**
     * Teaser ID
     */
    @Column(name = "teaser_id")
    private Long teaserId;

    /**
     * BP ID
     */
    @Column(name = "bp_id")
    private Long bpId;

    /**
     * 联系信息（申请联系企业时的联系方式）
     */
    @Column(name = "contact_info", length = 500)
    private String contactInfo;

    /**
     * 申请状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", length = 20)
    @Builder.Default
    private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

    /**
     * 申请理由
     */
    @Column(name = "application_reason", columnDefinition = "TEXT")
    private String applicationReason;

    /**
     * 拒绝原因
     */
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    /**
     * 管理员备注
     */
    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    /**
     * 审核人ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    /**
     * 审核时间
     */
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    /**
     * 申请过期时间（批准后的查看权限有效期）
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * 查看次数（批准后的查看统计）
     */
    @Column(name = "viewed_count")
    @Builder.Default
    private Integer viewedCount = 0;

    /**
     * 最后查看时间
     */
    @Column(name = "last_viewed_at")
    private LocalDateTime lastViewedAt;
}
