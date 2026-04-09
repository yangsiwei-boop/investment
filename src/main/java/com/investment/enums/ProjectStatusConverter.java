package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * ProjectStatus 枚举转换器
 * 处理数据库中大小写不一致的问题
 */
@Converter(autoApply = true)
public class ProjectStatusConverter implements AttributeConverter<ProjectStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProjectStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public ProjectStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return ProjectStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (ProjectStatus status : ProjectStatus.values()) {
                if (status.getCode().equalsIgnoreCase(dbData)) {
                    return status;
                }
            }
            throw e;
        }
    }
}
