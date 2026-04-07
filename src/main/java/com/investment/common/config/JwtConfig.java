package com.investment.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性
 *
 * @author Investment Team
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT签名密钥
     */
    private String secret;

    /**
     * Access Token 过期时间（毫秒）
     */
    private Long accessTokenExpiration;

    /**
     * Refresh Token 过期时间（毫秒）
     */
    private Long refreshTokenExpiration;

    /**
     * Token签发者
     */
    private String issuer;
}
