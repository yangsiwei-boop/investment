package com.investment.dto.request.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实名认证申请请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名不能超过50个字符")
    private String realName;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$",
            message = "身份证号格式不正确")
    private String idCardNumber;

    /**
     * 身份证正面照URL
     */
    @NotBlank(message = "身份证正面照不能为空")
    private String idCardFrontUrl;

    /**
     * 身份证反面照URL
     */
    @NotBlank(message = "身份证反面照不能为空")
    private String idCardBackUrl;

    /**
     * 认证类型（个人/企业）
     */
    @Builder.Default
    private String verificationType = "PERSONAL";

    /**
     * 营业执照URL（企业认证需要）
     */
    private String businessLicenseUrl;

    /**
     * 公司名称（企业认证需要）
     */
    private String companyName;
}
