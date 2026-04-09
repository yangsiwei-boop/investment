package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuestionStatusConverter implements AttributeConverter<QuestionStatus, String> {

    @Override
    public String convertToDatabaseColumn(QuestionStatus attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public QuestionStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return QuestionStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (QuestionStatus s : QuestionStatus.values()) {
                if (s.getCode().equalsIgnoreCase(dbData)) return s;
            }
            throw e;
        }
    }
}
