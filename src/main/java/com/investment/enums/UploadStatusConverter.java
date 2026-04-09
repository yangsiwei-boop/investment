package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UploadStatusConverter implements AttributeConverter<UploadStatus, String> {

    @Override
    public String convertToDatabaseColumn(UploadStatus attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public UploadStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return UploadStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (UploadStatus s : UploadStatus.values()) {
                if (s.getCode().equalsIgnoreCase(dbData)) return s;
            }
            throw e;
        }
    }
}
