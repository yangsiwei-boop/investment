package com.investment.common.constant;

/**
 * 应用常量
 *
 * @author Investment Team
 */
public class AppConstants {

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * 默认每页数量
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 最大每页数量
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * Token类型
     */
    public static final String TOKEN_TYPE_BEARER = "Bearer";

    /**
     * Token请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户状态
     */
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_BANNED = "BANNED";

    /**
     * 审核状态
     */
    public static final String REVIEW_STATUS_PENDING = "PENDING";
    public static final String REVIEW_STATUS_APPROVED = "APPROVED";
    public static final String REVIEW_STATUS_REJECTED = "REJECTED";

    /**
     * 文件上传路径
     */
    public static final String UPLOAD_PATH_AVATAR = "avatars";
    public static final String UPLOAD_PATH_BP = "bp";
    public static final String UPLOAD_PATH_TEASER = "teaser";
    public static final String UPLOAD_PATH_VERIFICATION = "verification";

    /**
     * 支持的文件类型
     */
    public static final String[] ALLOWED_IMAGE_TYPES = {
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    };

    public static final String[] ALLOWED_DOCUMENT_TYPES = {
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    };

    /**
     * 最大文件大小（字节）
     */
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * 验证码长度
     */
    public static final int VERIFICATION_CODE_LENGTH = 6;

    /**
     * 验证码过期时间（分钟）
     */
    public static final int VERIFICATION_CODE_EXPIRATION = 5;

    /**
     * JWT过期时间
     */
    public static final long ACCESS_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000; // 24小时
    public static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7天

    private AppConstants() {
        // 私有构造函数，防止实例化
    }
}
