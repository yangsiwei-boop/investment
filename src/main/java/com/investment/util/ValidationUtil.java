package com.investment.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author Investment Team
 */
@Slf4j
public class ValidationUtil {

    // 手机号正则（中国大陆）
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    // 邮箱正则
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    // 身份证号正则
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
            "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$"
    );

    // 统一社会信用代码正则
    private static final Pattern CREDIT_CODE_PATTERN = Pattern.compile(
            "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$"
    );

    // URL正则
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    );

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证身份证号
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        return idCard != null && ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 验证统一社会信用代码
     *
     * @param creditCode 统一社会信用代码
     * @return 是否有效
     */
    public static boolean isValidCreditCode(String creditCode) {
        return creditCode != null && CREDIT_CODE_PATTERN.matcher(creditCode).matches();
    }

    /**
     * 验证URL
     *
     * @param url URL
     * @return 是否有效
     */
    public static boolean isValidUrl(String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    /**
     * 验证字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 验证字符串是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 验证数值范围
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isInRange(Integer value, int min, int max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * 验证数值范围
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isInRange(Long value, long min, long max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * 验证数值是否为正数
     *
     * @param value 数值
     * @return 是否为正数
     */
    public static boolean isPositive(Integer value) {
        return value != null && value > 0;
    }

    /**
     * 验证数值是否为正数
     *
     * @param value 数值
     * @return 是否为正数
     */
    public static boolean isPositive(Long value) {
        return value != null && value > 0;
    }

    /**
     * 脱敏手机号
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 脱敏身份证号
     *
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "****" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 脱敏邮箱
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) {
            return email;
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}
