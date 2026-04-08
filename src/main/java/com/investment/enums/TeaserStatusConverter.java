package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * TeaserStatus 枚举转换器
 */
@Converter(autoApply = true)
public class TeaserStatusConverter implements AttributeConverter<TeaserStatus, String> {

    @Override
    public String convertToDatabaseColumn(TeaserStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TeaserStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return TeaserStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 尝试通过 code 匹配
            for (TeaserStatus status : TeaserStatus.values()) {
                if (status.getCode().equalsIgnoreCase(dbData)) {
                    return status;
                }
            }
            throw e;
        }
    }
}
