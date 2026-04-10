package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请类型枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum ApplicationType {
    GET_BP("get_bp", "获取BP"),
    CONTACT_COMPANY("contact_company", "联系企业"),
    VIEW_CONTACT("view_contact", "查看联系方式");

    private final String code;
    private final String description;

    /**
     * 根据 code 或枚举名获取 ApplicationType（不区分大小写）
     */
    public static ApplicationType fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("ApplicationType value cannot be null");
        }
        for (ApplicationType type : values()) {
            if (type.name().equalsIgnoreCase(value) || type.code.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ApplicationType: " + value);
    }
}
