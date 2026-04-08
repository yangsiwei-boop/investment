package com.investment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * UserType 枚举转换器
 * 处理数据库中大小写不一致的问题
 *
 * @author Investment Team
 */
@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public UserType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // 忽略大小写转换
        return UserType.valueOf(dbData.toUpperCase());
    }
}
