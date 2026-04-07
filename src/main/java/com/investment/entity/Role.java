package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 角色实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色代码
     */
    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, length = 100)
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * 角色级别（数字越大权限越大）
     */
    @Column(name = "role_level")
    @Builder.Default
    private Integer roleLevel = 0;

    /**
     * 是否系统角色（不可删除）
     */
    @Column(name = "is_system")
    @Builder.Default
    private Boolean isSystem = false;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    @Builder.Default
    private Boolean isEnabled = true;

    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    /**
     * 权限数量（冗余字段）
     */
    @Column(name = "permissions_count")
    @Builder.Default
    private Integer permissionsCount = 0;

    /**
     * 用户数量（冗余字段）
     */
    @Column(name = "users_count")
    @Builder.Default
    private Integer usersCount = 0;
}
