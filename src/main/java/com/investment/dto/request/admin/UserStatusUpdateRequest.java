package com.investment.dto.request.admin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户状态更新请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusUpdateRequest {

    /**
     * 用户状态
     */
    @NotNull(message = "用户状态不能为空")
    private String status;

    /**
     * 操作原因
     */
    private String reason;
}
