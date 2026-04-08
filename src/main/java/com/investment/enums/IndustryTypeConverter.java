package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * IndustryType 枚举转换器
 */
@Converter(autoApply = true)
public class IndustryTypeConverter implements AttributeConverter<IndustryType, String> {

    @Override
    public String convertToDatabaseColumn(IndustryType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public IndustryType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return IndustryType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 尝试通过 code 匹配
            for (IndustryType type : IndustryType.values()) {
                if (type.getCode().equalsIgnoreCase(dbData)) {
                    return type;
                }
            }
            throw e;
        }
    }
}
