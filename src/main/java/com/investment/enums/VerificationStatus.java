package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实名认证状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum VerificationStatus {
    PENDING("pending", "待审核"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝");

    private final String code;
    private final String description;
}
