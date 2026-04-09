package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApplicationStatusConverter implements AttributeConverter<ApplicationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationStatus attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public ApplicationStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return ApplicationStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (ApplicationStatus s : ApplicationStatus.values()) {
                if (s.getCode().equalsIgnoreCase(dbData)) return s;
            }
            throw e;
        }
    }
}
