package com.investment.dto.request.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 申请创建请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreateRequest {

    /**
     * Teaser ID
     */
    @NotNull(message = "Teaser ID不能为空")
    private Long teaserId;

    /**
     * 申请类型
     */
    @NotBlank(message = "申请类型不能为空")
    private String applicationType;

    /**
     * 申请理由
     */
    @Size(max = 1000, message = "申请理由不能超过1000个字符")
    private String reason;

    /**
     * 投资机构名称（申请联系方式时需要）
     */
    @Size(max = 100, message = "机构名称不能超过100个字符")
    private String institutionName;

    /**
     * 职位
     */
    @Size(max = 50, message = "职位不能超过50个字符")
    private String position;
}
