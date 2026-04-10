package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 融资阶段枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum FinancingStage {
    SEED("seed", "种子轮"),
    ANGEL("angel", "天使轮"),
    PRE_A("pre_a", "Pre-A轮"),
    A("a", "A轮"),
    B("b", "B轮"),
    C("c", "C轮"),
    D("d", "D轮"),
    PRE_IPO("pre_ipo", "Pre-IPO"),
    IPO("ipo", "IPO上市");

    private final String code;
    private final String description;

    public static FinancingStage fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (FinancingStage stage : values()) {
            if (stage.name().equalsIgnoreCase(value) || stage.code.equalsIgnoreCase(value)) {
                return stage;
            }
        }
        return null;
    }
}
