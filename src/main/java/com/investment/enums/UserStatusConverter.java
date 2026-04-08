package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * UserStatus 枚举转换器
 * 处理数据库中大小写不一致的问题
 *
 * @author Investment Team
 */
@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // 忽略大小写转换
        return UserStatus.valueOf(dbData.toUpperCase());
    }
}
