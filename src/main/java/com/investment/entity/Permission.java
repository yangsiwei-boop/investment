package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 权限实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限代码
     */
    @Column(name = "permission_code", nullable = false, unique = true, length = 50)
    private String permissionCode;

    /**
     * 权限名称
     */
    @Column(name = "permission_name", nullable = false, length = 100)
    private String permissionName;

    /**
     * 权限描述
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * 所属模块
     */
    @Column(name = "module", length = 50)
    private String module;

    /**
     * 父权限ID（用于权限分组）
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 权限类型：menu-菜单,button-按钮,api-API接口,data-数据
     */
    @Column(name = "permission_type", length = 20)
    @Builder.Default
    private String permissionType = "menu";

    /**
     * 资源路径（API路径等）
     */
    @Column(name = "resource_path", length = 255)
    private String resourcePath;

    /**
     * HTTP方法（API权限）
     */
    @Column(name = "http_methods", length = 50)
    private String httpMethods;

    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    @Builder.Default
    private Boolean isEnabled = true;

    /**
     * 图标（菜单权限）
     */
    @Column(name = "icon", length = 50)
    private String icon;
}
