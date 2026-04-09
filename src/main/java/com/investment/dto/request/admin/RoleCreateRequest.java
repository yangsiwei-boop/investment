package com.investment.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色创建请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码最长50个字符")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称最长100个字符")
    private String roleName;

    @Size(max = 255, message = "描述最长255个字符")
    private String description;

    private Integer roleLevel;

    private Boolean isEnabled;

    private Integer sortOrder;

    private List<Long> permissionIds;
}
