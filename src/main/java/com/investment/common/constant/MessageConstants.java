package com.investment.common.constant;

/**
 * 消息常量
 *
 * @author Investment Team
 */
public class MessageConstants {

    // 成功消息
    public static final String SUCCESS = "操作成功";
    public static final String CREATE_SUCCESS = "创建成功";
    public static final String UPDATE_SUCCESS = "更新成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String UPLOAD_SUCCESS = "上传成功";
    public static final String SEND_SUCCESS = "发送成功";

    // 错误消息
    public static final String SYSTEM_ERROR = "系统错误，请稍后重试";
    public static final String NETWORK_ERROR = "网络错误，请稍后重试";
    public static final String DATABASE_ERROR = "数据库错误，请稍后重试";

    // 验证消息
    public static final String PARAM_ERROR = "参数错误";
    public static final String PARAM_EMPTY = "参数不能为空";
    public static final String PARAM_FORMAT_ERROR = "参数格式不正确";
    public static final String PARAM_RANGE_ERROR = "参数值超出范围";

    // 认证消息
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGOUT_SUCCESS = "登出成功";
    public static final String LOGIN_ERROR = "用户名或密码错误";
    public static final String TOKEN_EXPIRED = "登录已过期，请重新登录";
    public static final String TOKEN_INVALID = "无效的登录凭证";
    public static final String PERMISSION_DENIED = "无权限执行此操作";

    // 用户消息
    public static final String USER_NOT_FOUND = "用户不存在";
    public static final String USER_EXISTS = "用户已存在";
    public static final String USER_DISABLED = "用户已被禁用";
    public static final String PASSWORD_ERROR = "密码错误";

    // 文件消息
    public static final String FILE_NOT_FOUND = "文件不存在";
    public static final String FILE_UPLOAD_ERROR = "文件上传失败";
    public static final String FILE_SIZE_ERROR = "文件大小超过限制";
    public static final String FILE_TYPE_ERROR = "不支持的文件类型";

    // 业务消息
    public static final String PROJECT_NOT_FOUND = "项目不存在";
    public static final String TEASER_NOT_FOUND = "Teaser不存在";
    public static final String BP_NOT_FOUND = "商业计划书不存在";
    public static final String APPLICATION_NOT_FOUND = "申请不存在";
    public static final String QUESTION_NOT_FOUND = "问题不存在";

    private MessageConstants() {
    }
}
