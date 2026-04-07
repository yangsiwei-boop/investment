package com.investment.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举类
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ==================== 通用错误码 (1xxxx) ====================
    BAD_REQUEST(400, "请求参数错误"),
    MISSING_PARAMETER(10002, "缺少必填参数"),
    INVALID_PARAMETER_FORMAT(10003, "参数格式不正确"),
    PARAMETER_OUT_OF_RANGE(10004, "参数值超出允许范围"),
    INVALID_JSON(10005, "请求体JSON格式错误"),

    // ==================== 认证授权错误码 (2xxxx) ====================
    UNAUTHORIZED(401, "未登录，请先登录"),
    TOKEN_EXPIRED(20002, "Token已过期，请重新登录"),
    TOKEN_INVALID(20003, "Token无效"),
    ACCESS_DENIED(403, "无权限访问此资源"),
    REFRESH_TOKEN_INVALID(20005, "Refresh Token无效或已过期"),
    ACCOUNT_DISABLED(20006, "账号已被禁用"),
    ACCOUNT_BANNED(20007, "账号已被封禁"),
    ACCOUNT_INACTIVE(20008, "账号未激活"),
    PASSWORD_ERROR(20009, "密码错误"),
    PASSWORD_EXPIRED(20010, "密码已过期，需要修改密码"),

    // ==================== 用户相关错误码 (3xxxx) ====================
    USER_NOT_FOUND(30001, "用户不存在"),
    USER_ALREADY_EXISTS(30002, "用户已存在"),
    PHONE_ALREADY_REGISTERED(30003, "手机号已注册"),
    VERIFICATION_CODE_ERROR(30004, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(30005, "验证码已过期"),
    VERIFICATION_CODE_TOO_FREQUENT(30006, "验证码发送过于频繁"),
    OLD_PASSWORD_ERROR(30007, "原密码错误"),
    NEW_PASSWORD_SAME_AS_OLD(30008, "新密码不能与旧密码相同"),
    VERIFICATION_PENDING(30009, "实名认证审核中，请勿重复提交"),
    VERIFICATION_NOT_APPROVED(30010, "实名认证未通过"),

    // ==================== 项目相关错误码 (4xxxx) ====================
    PROJECT_NOT_FOUND(40001, "项目不存在"),
    PROJECT_DELETED(40002, "项目已删除"),
    PROJECT_ACCESS_DENIED(40003, "无权限操作此项目"),
    TEASER_NOT_FOUND(40004, "Teaser不存在"),
    TEASER_ALREADY_PUBLISHED(40005, "Teaser已发布，不能编辑"),
    BP_NOT_FOUND(40006, "BP文件不存在"),
    BP_FORMAT_NOT_SUPPORTED(40007, "BP文件格式不支持"),
    BP_SIZE_EXCEEDED(40008, "BP文件超过大小限制"),
    TEASER_GENERATION_FAILED(40009, "Teaser生成失败"),
    CONTENT_EXTRACTION_FAILED(40010, "内容提取失败"),
    TEASER_NOT_AVAILABLE(40011, "Teaser当前不可访问"),
    TEASER_ALREADY_EXISTS(40012, "Teaser已存在"),
    CANNOT_MODIFY_PUBLISHED_TEASER(40013, "已发布的Teaser不能修改"),
    TEASER_TITLE_REQUIRED(40014, "Teaser标题不能为空"),
    TEASER_SUMMARY_REQUIRED(40015, "Teaser摘要不能为空"),
    BUSINESS_PLAN_NOT_FOUND(40016, "商业计划书不存在"),
    FILE_EMPTY(40017, "文件内容为空"),
    FILE_TOO_LARGE(40018, "文件大小超过限制"),
    INVALID_FILE_TYPE(40019, "不支持的文件类型"),
    ALREADY_FAVORITED(40020, "已收藏，请勿重复操作"),
    FAVORITE_NOT_FOUND(40021, "收藏记录不存在"),
    PROFILE_NOT_FOUND(40022, "用户资料不存在"),

    // ==================== 申请审核错误码 (5xxxx) ====================
    APPLICATION_NOT_FOUND(50001, "申请不存在"),
    APPLICATION_ALREADY_REVIEWED(50002, "申请已审核，不能重复审核"),
    APPLICATION_EXPIRED(50003, "申请已过期"),
    APPLICATION_REVIEW_DENIED(50004, "无权限审核此申请"),
    APPLICATION_ALREADY_EXISTS(50005, "申请已存在，请勿重复提交"),
    BP_ACCESS_DENIED(50005, "未通过审核，无法查看BP"),
    CONTACT_ACCESS_DENIED(50006, "未通过审核，无法查看联系方式"),

    // ==================== 问答相关错误码 (6xxxx) ====================
    QA_RECORD_NOT_FOUND(60001, "问答记录不存在"),
    QUESTION_ALREADY_ANSWERED(60002, "问题已回答，不能重复回答"),
    QUESTION_WITHDRAWN(60003, "问题已撤回，不能回答"),
    CANNOT_ANSWER_OWN_QUESTION(60004, "不能回复自己的问题"),
    QUESTION_LIBRARY_NOT_FOUND(60005, "问题库问题不存在"),
    NOTIFICATION_NOT_FOUND(60006, "通知不存在"),

    // ==================== AI服务错误码 (7xxxx) ====================
    AI_ANALYSIS_NOT_FOUND(70001, "AI分析任务不存在"),
    AI_ANALYSIS_PROCESSING(70002, "AI分析任务正在处理中"),
    AI_ANALYSIS_FAILED(70003, "AI分析任务已失败"),
    AI_ANALYSIS_LIMIT_EXCEEDED(70004, "AI分析次数已达上限"),
    AI_SERVICE_UNAVAILABLE(70005, "AI服务暂时不可用"),
    AI_ANALYSIS_TIMEOUT(70006, "AI分析生成超时"),
    ANALYSIS_NOT_FOUND(70007, "分析记录不存在"),
    ANALYSIS_ALREADY_EXISTS(70008, "分析记录已存在"),
    NO_PERMISSION(70009, "无权限操作"),
    VERIFICATION_NOT_FOUND(70010, "认证记录不存在"),
    REFRESH_TOKEN_EXPIRED(20011, "Refresh Token已过期"),

    // ==================== 文件相关错误码 (8xxxx) ====================
    FILE_NOT_FOUND(80001, "文件不存在"),
    FILE_UPLOAD_FAILED(80002, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED(80003, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(80004, "文件超过大小限制"),
    FILE_DOWNLOAD_FAILED(80005, "文件下载失败"),

    // ==================== 系统错误码 (9xxxx) ====================
    INTERNAL_ERROR(500, "系统内部错误"),
    DATABASE_ERROR(90002, "数据库错误"),
    THIRD_PARTY_SERVICE_ERROR(90003, "第三方服务错误"),
    SERVICE_UNAVAILABLE(90004, "服务暂时不可用"),
    SYSTEM_MAINTENANCE(90005, "系统维护中");

    private final Integer code;
    private final String message;
}
