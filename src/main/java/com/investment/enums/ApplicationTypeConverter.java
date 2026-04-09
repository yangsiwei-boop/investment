package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApplicationTypeConverter implements AttributeConverter<ApplicationType, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationType attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public ApplicationType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return ApplicationType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (ApplicationType t : ApplicationType.values()) {
                if (t.getCode().equalsIgnoreCase(dbData)) return t;
            }
            throw e;
        }
    }
}
