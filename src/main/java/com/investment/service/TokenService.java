package com.investment.service;

import com.investment.common.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token缓存服务
 * 使用Redis存储Token相关信息
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存用户Token
     *
     * @param userId    用户ID
     * @param token     访问令牌
     * @param expiresIn 过期时间（秒）
     */
    public void cacheAccessToken(Long userId, String token, long expiresIn) {
        String key = CacheConstants.TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, expiresIn, TimeUnit.SECONDS);
        log.debug("Cached access token for user: {}", userId);
    }

    /**
     * 获取用户缓存的Token
     *
     * @param userId 用户ID
     * @return Token字符串，不存在返回null
     */
    public String getCachedAccessToken(Long userId) {
        String key = CacheConstants.TOKEN_PREFIX + userId;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 缓存RefreshToken
     *
     * @param userId       用户ID
     * @param refreshToken 刷新令牌
     * @param expiresIn    过期时间（秒）
     */
    public void cacheRefreshToken(Long userId, String refreshToken, long expiresIn) {
        String key = CacheConstants.REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, expiresIn, TimeUnit.SECONDS);
        log.debug("Cached refresh token for user: {}", userId);
    }

    /**
     * 获取缓存的RefreshToken
     *
     * @param userId 用户ID
     * @return RefreshToken字符串
     */
    public String getCachedRefreshToken(Long userId) {
        String key = CacheConstants.REFRESH_TOKEN_PREFIX + userId;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 验证RefreshToken是否有效
     *
     * @param userId       用户ID
     * @param refreshToken 刷新令牌
     * @return 是否有效
     */
    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String cached = getCachedRefreshToken(userId);
        return refreshToken.equals(cached);
    }

    /**
     * 将Token加入黑名单（用于登出）
     *
     * @param token    Token字符串
     * @param expiresIn 过期时间（秒）
     */
    public void addToBlacklist(String token, long expiresIn) {
        String key = CacheConstants.TOKEN_BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "1", expiresIn, TimeUnit.SECONDS);
        log.debug("Added token to blacklist");
    }

    /**
     * 检查Token是否在黑名单中
     *
     * @param token Token字符串
     * @return 是否在黑名单中
     */
    public boolean isBlacklisted(String token) {
        try {
            String key = CacheConstants.TOKEN_BLACKLIST_PREFIX + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis error when checking blacklist: {}", e.getMessage());
            return false; // Redis异常时不阻止访问
        }
    }

    /**
     * 清除用户的所有Token（用于登出）
     *
     * @param userId 用户ID
     */
    public void clearUserTokens(Long userId) {
        String accessTokenKey = CacheConstants.TOKEN_PREFIX + userId;
        String refreshTokenKey = CacheConstants.REFRESH_TOKEN_PREFIX + userId;
        String userInfoKey = CacheConstants.USER_INFO_PREFIX + userId;
        redisTemplate.delete(accessTokenKey);
        redisTemplate.delete(refreshTokenKey);
        redisTemplate.delete(userInfoKey);
        log.debug("Cleared all tokens for user: {}", userId);
    }

    /**
     * 缓存用户信息
     *
     * @param userId   用户ID
     * @param userInfo 用户信息对象
     * @param expiresIn 过期时间（秒）
     */
    public void cacheUserInfo(Long userId, Object userInfo, long expiresIn) {
        String key = CacheConstants.USER_INFO_PREFIX + userId;
        redisTemplate.opsForValue().set(key, userInfo, expiresIn, TimeUnit.SECONDS);
        log.debug("Cached user info for user: {}", userId);
    }

    /**
     * 获取缓存的用户信息
     *
     * @param userId 用户ID
     * @return 用户信息对象
     */
    public Object getCachedUserInfo(Long userId) {
        String key = CacheConstants.USER_INFO_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 清除用户信息缓存
     *
     * @param userId 用户ID
     */
    public void clearUserInfo(Long userId) {
        String key = CacheConstants.USER_INFO_PREFIX + userId;
        redisTemplate.delete(key);
        log.debug("Cleared user info cache for user: {}", userId);
    }
}
