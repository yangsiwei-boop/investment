package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Teaser状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum TeaserStatus {
    DRAFT("draft", "草稿"),
    PUBLISHED("published", "已发布"),
    HIDDEN("hidden", "已隐藏");

    private final String code;
    private final String description;
}
