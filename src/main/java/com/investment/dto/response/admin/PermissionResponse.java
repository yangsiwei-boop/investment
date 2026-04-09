package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;

    private String permissionCode;

    private String permissionName;

    private String description;

    private String module;

    private Long parentId;

    private String permissionType;

    private String resourcePath;

    private String httpMethods;

    private Integer sortOrder;

    private Boolean isEnabled;

    private String icon;
}
