package com.investment.entity;

import com.investment.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 实名认证实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_verifications")
public class UserVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 认证类型
     */
    @Column(name = "verification_type", nullable = false, length = 20)
    private String verificationType;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    /**
     * 身份证号
     */
    @Column(name = "id_card_number", length = 18)
    private String idCardNumber;

    /**
     * 身份证正面照片URL
     */
    @Column(name = "id_card_front_url", length = 500)
    private String idCardFrontUrl;

    /**
     * 身份证背面照片URL
     */
    @Column(name = "id_card_back_url", length = 500)
    private String idCardBackUrl;

    /**
     * 营业执照URL
     */
    @Column(name = "business_license_url", length = 500)
    private String businessLicenseUrl;

    /**
     * 名片URL
     */
    @Column(name = "business_card_url", length = 500)
    private String businessCardUrl;

    /**
     * 其他证明材料（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_documents", columnDefinition = "json")
    private String additionalDocuments;

    /**
     * 公司名称
     */
    @Column(name = "company_name", length = 200)
    private String companyName;

    /**
     * 职位
     */
    @Column(name = "position", length = 100)
    private String position;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    /**
     * 审核状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", length = 20)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    /**
     * 拒绝原因
     */
    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;

    /**
     * 管理员备注
     */
    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    /**
     * 审核人ID
     */
    @Column(name = "reviewed_by")
    private Long reviewedBy;

    /**
     * 审核时间
     */
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    /**
     * 认证有效期
     */
    @Column(name = "verified_until")
    private LocalDate verifiedUntil;

    /**
     * 提交来源
     */
    @Column(name = "submission_source", length = 20)
    @Builder.Default
    private String submissionSource = "web";

    /**
     * 提交IP地址
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
}
