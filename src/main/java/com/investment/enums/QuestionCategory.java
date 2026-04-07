package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 问题分类枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum QuestionCategory {
    BUSINESS_MODEL("business_model", "商业模式"),
    PRODUCT_TECHNOLOGY("product_technology", "产品与技术"),
    MARKET_EXPANSION("market_expansion", "市场拓展"),
    FINANCIAL_DATA("financial_data", "财务数据"),
    TEAM("team", "团队背景"),
    FINANCING_PLAN("financing_plan", "融资计划"),
    COMPETITION("competition", "竞争对手"),
    OPERATION("operation", "运营数据"),
    OTHER("other", "其他");

    private final String code;
    private final String description;
}
