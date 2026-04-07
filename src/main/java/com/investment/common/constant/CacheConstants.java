package com.investment.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * Redis缓存键常量
 *
 * @author Investment Team
 */
public class CacheConstants {

    /**
     * 缓存键前缀
     */
    public static final String CACHE_PREFIX = "investment:";

    /**
     * 用户缓存
     */
    public static final String USER_CACHE = CACHE_PREFIX + "users";
    public static final String USER_TOKEN_CACHE = CACHE_PREFIX + "user_tokens";

    /**
     * 配置缓存
     */
    public static final String SYSTEM_CONFIG_CACHE = CACHE_PREFIX + "system_configs";

    /**
     * Teaser缓存
     */
    public static final String TEASER_CACHE = CACHE_PREFIX + "teasers";
    public static final String TEASER_LIST_CACHE = CACHE_PREFIX + "teaser_list";

    /**
     * 项目缓存
     */
    public static final String PROJECT_CACHE = CACHE_PREFIX + "projects";

    /**
     * 推荐缓存
     */
    public static final String RECOMMENDATION_CACHE = CACHE_PREFIX + "recommendations";

    /**
     * 统计缓存
     */
    public static final String STATISTICS_CACHE = CACHE_PREFIX + "statistics";
    public static final String DASHBOARD_CACHE = CACHE_PREFIX + "dashboard";

    /**
     * 验证码缓存
     */
    public static final String VERIFICATION_CODE_CACHE = CACHE_PREFIX + "verification_codes";

    /**
     * 默认缓存过期时间（秒）
     */
    public static final long DEFAULT_CACHE_TTL = 3600; // 1小时
    public static final long SHORT_CACHE_TTL = 300; // 5分钟
    public static final long LONG_CACHE_TTL = 86400; // 24小时

    private CacheConstants() {
    }
}
