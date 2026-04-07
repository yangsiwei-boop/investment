package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分析状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum AnalysisStatus {
    PENDING("pending", "待处理"),
    PROCESSING("processing", "处理中"),
    COMPLETED("completed", "已完成"),
    FAILED("failed", "失败");

    private final String code;
    private final String description;
}
