package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * FinancingStage 枚举转换器
 */
@Converter(autoApply = true)
public class FinancingStageConverter implements AttributeConverter<FinancingStage, String> {

    @Override
    public String convertToDatabaseColumn(FinancingStage attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public FinancingStage convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return FinancingStage.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 尝试通过 code 匹配
            for (FinancingStage stage : FinancingStage.values()) {
                if (stage.getCode().equalsIgnoreCase(dbData)) {
                    return stage;
                }
            }
            throw e;
        }
    }
}
