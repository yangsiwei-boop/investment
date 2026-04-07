package com.investment.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证审核请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationReviewRequest {

    /**
     * 是否通过
     */
    private boolean approved;

    /**
     * 审核意见
     */
    @Size(max = 500, message = "审核意见不能超过500个字符")
    private String comment;
}
