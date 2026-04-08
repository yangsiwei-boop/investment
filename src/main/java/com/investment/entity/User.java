package com.investment.entity;

import com.investment.enums.UserStatus;
import com.investment.enums.UserStatusConverter;
import com.investment.enums.UserType;
import com.investment.enums.UserTypeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 手机号
     */
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    /**
     * 密码哈希
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 用户类型
     */
    @Convert(converter = UserTypeConverter.class)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 账号状态
     */
    @Convert(converter = UserStatusConverter.class)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20)")
    @Builder.Default
    private UserStatus status = UserStatus.PENDING;

    /**
     * 是否实名认证
     */
    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 最后查看的项目ID
     */
    @Column(name = "last_viewed_project_id")
    private Long lastViewedProjectId;

    /**
     * 最后查看的Teaser ID
     */
    @Column(name = "last_viewed_teaser_id")
    private Long lastViewedTeaserId;

    /**
     * 未读通知数量
     */
    @Column(name = "notification_count")
    @Builder.Default
    private Integer notificationCount = 0;

    /**
     * 未读问题数量（融资用户）
     */
    @Column(name = "unread_question_count")
    @Builder.Default
    private Integer unreadQuestionCount = 0;

    /**
     * 资料完善度百分比
     */
    @Column(name = "profile_completion_rate")
    @Builder.Default
    private Integer profileCompletionRate = 0;

    /**
     * 用户偏好设置（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "json")
    private String preferences;
}
