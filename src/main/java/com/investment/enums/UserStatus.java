package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("active", "正常"),
    PENDING("pending", "待激活"),
    INACTIVE("inactive", "未激活"),
    BANNED("banned", "已封禁");

    private final String code;
    private final String description;
}
