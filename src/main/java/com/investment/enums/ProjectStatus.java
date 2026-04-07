package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum ProjectStatus {
    DRAFT("draft", "草稿"),
    PUBLISHED("published", "已发布"),
    HIDDEN("hidden", "已隐藏"),
    ARCHIVED("archived", "已归档");

    private final String code;
    private final String description;
}
