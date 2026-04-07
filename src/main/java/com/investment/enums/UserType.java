package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum UserType {
    INVESTOR("investor", "投资人"),
    ENTREPRENEUR("entrepreneur", "融资用户"),
    ADMIN("admin", "管理员");

    private final String code;
    private final String description;
}
