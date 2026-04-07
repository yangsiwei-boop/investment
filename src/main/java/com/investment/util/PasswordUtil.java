package com.investment.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * 密码工具类
 *
 * @author Investment Team
 */
@Slf4j
@Component
public class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final SecureRandom random = new SecureRandom();

    // 密码强度正则：至少8位，包含大小写字母、数字和特殊字符
    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    // 密码基本正则：至少6位，包含字母和数字
    private static final Pattern BASIC_PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
    );

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&";
        StringBuilder password = new StringBuilder();

        // 确保包含各种类型的字符
        password.append(chars.charAt(random.nextInt(26))); // 大写字母
        password.append(chars.charAt(26 + random.nextInt(26))); // 小写字母
        password.append(chars.charAt(52 + random.nextInt(10))); // 数字
        password.append(chars.charAt(62 + random.nextInt(6))); // 特殊字符

        // 填充剩余长度
        for (int i = 4; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        // 打乱顺序
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }

    /**
     * 检查密码强度
     *
     * @param password 密码
     * @return 强度等级（WEAK, MEDIUM, STRONG）
     */
    public static String checkPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return "WEAK";
        }

        if (STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
            return "STRONG";
        } else if (BASIC_PASSWORD_PATTERN.matcher(password).matches()) {
            return "MEDIUM";
        } else {
            return "WEAK";
        }
    }

    /**
     * 验证密码是否符合基本要求
     *
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 20) {
            return false;
        }
        return BASIC_PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 生成验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public static String generateVerificationCode(int length) {
        String digits = "0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(digits.charAt(random.nextInt(digits.length())));
        }
        return code.toString();
    }
}
