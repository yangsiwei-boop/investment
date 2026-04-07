package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.auth.LoginRequest;
import com.investment.dto.request.auth.RegisterRequest;
import com.investment.dto.response.auth.LoginResponse;
import com.investment.dto.response.auth.RegisterResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录、密码管理相关接口")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册响应
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for phone: {}", request.getPhone());
        RegisterResponse response = authService.register(request);
        return ApiResponse.success("注册成功", response);
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取Token")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for phone: {}", request.getPhone());
        LoginResponse response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String newToken = authService.refreshToken(request.getRefreshToken());
        return ApiResponse.success("刷新成功", TokenResponse.builder()
                .token(newToken)
                .expiresAt(System.currentTimeMillis() + 7200000L)
                .build());
    }

    /**
     * 用户登出
     *
     * @param principal 当前用户
     * @param request HTTP请求（用于获取Token）
     * @return 成功响应
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录，将Token加入黑名单")
    public ApiResponse<Void> logout(
            @AuthenticationPrincipal UserPrincipal principal,
            HttpServletRequest request) {
        if (principal != null) {
            // 从请求头获取Token
            String token = getJwtFromRequest(request);
            authService.logout(principal.getId(), token);
        }
        return ApiResponse.success("登出成功", null);
    }

    /**
     * 从请求头中获取JWT Token
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 修改密码
     *
     * @param principal 当前用户
     * @param request 修改密码请求
     * @return 成功响应
     */
    @PostMapping("/password/change")
    @Operation(summary = "修改密码", description = "修改用户密码")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(principal.getId(), request.getOldPassword(), request.getNewPassword());
        return ApiResponse.success("密码修改成功", null);
    }

    /**
     * Token响应DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TokenResponse {
        private String token;
        private Long expiresAt;
    }

    /**
     * 刷新Token请求
     */
    @lombok.Data
    public static class RefreshTokenRequest {
        private String refreshToken;
    }

    /**
     * 修改密码请求
     */
    @lombok.Data
    public static class ChangePasswordRequest {
        @jakarta.validation.constraints.NotBlank(message = "旧密码不能为空")
        private String oldPassword;
        @jakarta.validation.constraints.NotBlank(message = "新密码不能为空")
        @jakarta.validation.constraints.Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
                message = "密码必须为8-20位，包含字母和数字")
        private String newPassword;
    }
}
