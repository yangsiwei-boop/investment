package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * NotificationType 枚举转换器
 * 处理数据库中大小写不一致的问题
 *
 * @author Investment Team
 */
@Converter(autoApply = true)
public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {

    @Override
    public String convertToDatabaseColumn(NotificationType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public NotificationType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // 先尝试code匹配
        for (NotificationType t : NotificationType.values()) {
            if (t.getCode().equalsIgnoreCase(dbData)) {
                return t;
            }
        }
        // 再尝试name匹配
        return NotificationType.valueOf(dbData.toUpperCase());
    }
}
