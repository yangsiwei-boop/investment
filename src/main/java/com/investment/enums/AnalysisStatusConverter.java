package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AnalysisStatusConverter implements AttributeConverter<AnalysisStatus, String> {

    @Override
    public String convertToDatabaseColumn(AnalysisStatus attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public AnalysisStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return AnalysisStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (AnalysisStatus s : AnalysisStatus.values()) {
                if (s.getCode().equalsIgnoreCase(dbData)) return s;
            }
            throw e;
        }
    }
}
