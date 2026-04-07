package com.investment.service;

import com.investment.common.constant.CacheConstants;
import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.auth.LoginRequest;
import com.investment.dto.request.auth.RegisterRequest;
import com.investment.dto.response.auth.LoginResponse;
import com.investment.dto.response.auth.RegisterResponse;
import com.investment.entity.User;
import com.investment.enums.UserStatus;
import com.investment.enums.UserType;
import com.investment.repository.UserRepository;
import com.investment.security.JwtTokenProvider;
import com.investment.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务类
 *
 * @author Investment Team
 */
@Slf4j@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册响应
     */
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest request) {
        log.info("User registering with phone: {}", request.getPhone());

        // 检查手机号是否已注册
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_REGISTERED);
        }

        // 创建用户
        User user = User.builder()
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .userType(UserType.valueOf(request.getUserType().toUpperCase()))
                .status(UserStatus.PENDING)
                .isVerified(false)
                .notificationCount(0)
                .unreadQuestionCount(0)
                .profileCompletionRate(0)
                .build();

        user = userRepository.save(user);

        // 生成Token
        String token = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserType().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        // 缓存Token到Redis
        tokenService.cacheAccessToken(user.getId(), token, CacheConstants.ACCESS_TOKEN_TTL);
        tokenService.cacheRefreshToken(user.getId(), refreshToken, CacheConstants.REFRESH_TOKEN_TTL);

        log.info("User registered successfully: userId={}", user.getId());

        return RegisterResponse.builder()
                .userId(user.getId())
                .token(token)
                .refreshToken(refreshToken)
                .userType(user.getUserType().name())
                .expiresAt(System.currentTimeMillis() + CacheConstants.ACCESS_TOKEN_TTL * 1000)
                .build();
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        log.info("User logging in with phone: {}", request.getPhone());

        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 检查账号状态
        if (user.getStatus() == UserStatus.BANNED) {
            throw new BusinessException(ErrorCode.ACCOUNT_BANNED);
        }

        // 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // 生成Token
        String token = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserType().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        // 缓存Token到Redis
        tokenService.cacheAccessToken(user.getId(), token, CacheConstants.ACCESS_TOKEN_TTL);
        tokenService.cacheRefreshToken(user.getId(), refreshToken, CacheConstants.REFRESH_TOKEN_TTL);

        // 缓存用户信息
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .userType(user.getUserType().name())
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .isVerified(user.getIsVerified())
                .status(user.getStatus().name())
                .build();
        tokenService.cacheUserInfo(user.getId(), userInfo, CacheConstants.ACCESS_TOKEN_TTL);

        log.info("User logged in successfully: userId={}", user.getId());

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();
    }

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    public String refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 验证RefreshToken是否在Redis中有效
        if (!tokenService.validateRefreshToken(userId, refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserType().name());

        // 缓存新Token
        tokenService.cacheAccessToken(user.getId(), newToken, CacheConstants.ACCESS_TOKEN_TTL);

        return newToken;
    }

    /**
     * 用户登出
     *
     * @param userId 用户ID
     * @param token  当前Token
     */
    public void logout(Long userId, String token) {
        log.info("User logging out: userId={}", userId);

        // 将当前Token加入黑名单
        if (token != null) {
            tokenService.addToBlacklist(token, CacheConstants.ACCESS_TOKEN_TTL);
        }

        // 清除用户的所有Token
        tokenService.clearUserTokens(userId);

        log.info("User logged out successfully: userId={}", userId);
    }

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.OLD_PASSWORD_ERROR);
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 清除用户Token缓存，强制重新登录
        tokenService.clearUserTokens(userId);

        log.info("Password changed for userId={}", userId);
    }
}
