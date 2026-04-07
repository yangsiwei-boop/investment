package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 系统配置实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_configs")
public class SystemConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置key
     */
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    /**
     * 配置分类
     */
    @Column(name = "category", length = 50)
    private String category;

    /**
     * 配置值
     */
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    /**
     * 验证规则
     */
    @Column(name = "validation_rule", length = 255)
    private String validationRule;

    /**
     * 默认值
     */
    @Column(name = "default_value", columnDefinition = "TEXT")
    private String defaultValue;

    /**
     * 配置类型
     */
    @Column(name = "config_type", length = 20)
    @Builder.Default
    private String configType = "string";

    /**
     * 配置描述
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * 是否公开(前端可访问)
     */
    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = false;

    /**
     * 是否可编辑
     */
    @Column(name = "is_editable")
    @Builder.Default
    private Boolean isEditable = true;

    /**
     * 是否系统配置（不可删除）
     */
    @Column(name = "is_system")
    @Builder.Default
    private Boolean isSystem = false;

    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    /**
     * 环境作用域
     */
    @Column(name = "env_scope", length = 20)
    @Builder.Default
    private String envScope = "all";
}
