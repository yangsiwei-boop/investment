package com.investment.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 短信验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String code;

    /**
     * 密码（8-20位，包含字母和数字）
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "密码必须为8-20位，包含字母和数字")
    private String password;

    /**
     * 用户类型：INVESTOR/ENTREPRENEUR
     */
    @NotBlank(message = "用户类型不能为空")
    @Pattern(regexp = "^(INVESTOR|ENTREPRENEUR)$", message = "用户类型不正确")
    private String userType;

    /**
     * 是否同意用户协议
     */
    private Boolean agreedToTerms;
}
