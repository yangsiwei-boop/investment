package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long id;

    private String roleCode;

    private String roleName;

    private String description;

    private Integer roleLevel;

    private Boolean isSystem;

    private Boolean isEnabled;

    private Integer sortOrder;

    private Integer permissionsCount;

    private Integer usersCount;

    private List<PermissionResponse> permissions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
