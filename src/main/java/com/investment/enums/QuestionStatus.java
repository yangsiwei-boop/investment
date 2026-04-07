package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 问题状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum QuestionStatus {
    DRAFT("draft", "草稿"),
    PENDING("pending", "待回复"),
    ANSWERED("answered", "已回复"),
    IGNORED("ignored", "已忽略"),
    WITHDRAWN("withdrawn", "已撤回");

    private final String code;
    private final String description;
}
