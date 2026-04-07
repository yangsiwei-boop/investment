package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum ApplicationStatus {
    PENDING("pending", "待审核"),
    APPROVED("approved", "已批准"),
    REJECTED("rejected", "已拒绝"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String description;
}
