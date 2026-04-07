package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统配置响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigResponse {

    /**
     * 配置ID
     */
    private Long id;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置分组
     */
    private String configGroup;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 配置类型
     */
    private String configType;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 是否可编辑
     */
    private Boolean isEditable;

    /**
     * 是否系统配置
     */
    private Boolean isSystem;

    /**
     * 排序序号
     */
    private Integer sortOrder;
}
