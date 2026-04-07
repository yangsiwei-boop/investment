package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 行业类型枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum IndustryType {
    ENTERPRISE_SERVICE("enterprise_service", "企业服务"),
    AI("ai", "人工智能"),
    FINTECH("fintech", "金融科技"),
    HEALTHCARE("healthcare", "医疗健康"),
    CONSUMER("consumer", "消费零售"),
    NEW_ENERGY("new_energy", "新能源"),
    EDUCATION("education", "教育培训"),
    ENTERTAINMENT("entertainment", "游戏娱乐"),
    MANUFACTURING("manufacturing", "制造业"),
    OTHER("other", "其他");

    private final String code;
    private final String description;
}
