package com.investment.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色更新请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {

    private String roleName;

    private String description;

    private Integer roleLevel;

    private Boolean isEnabled;

    private Integer sortOrder;

    private List<Long> permissionIds;
}
