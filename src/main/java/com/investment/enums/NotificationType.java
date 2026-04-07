package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum NotificationType {
    SYSTEM("system", "系统通知"),
    NEW_QUESTION("new_question", "新问题"),
    QUESTION_RECEIVED("question_received", "收到问题"),
    QUESTION_ANSWERED("question_answered", "问题已回答"),
    NEW_APPLICATION("new_application", "新申请"),
    APPLICATION_REVIEWED("application_reviewed", "申请已审核"),
    BP_REQUEST_APPROVED("bp_request_approved", "BP申请已批准"),
    BP_REQUEST_REJECTED("bp_request_rejected", "BP申请已拒绝"),
    CONTACT_REQUEST_APPROVED("contact_request_approved", "联系方式申请已批准"),
    CONTACT_REQUEST_REJECTED("contact_request_rejected", "联系方式申请已拒绝"),
    VERIFICATION_APPROVED("verification_approved", "实名认证已通过"),
    VERIFICATION_REJECTED("verification_rejected", "实名认证已拒绝"),
    NEW_PROJECT("new_project", "新项目"),
    NEW_TEASER("new_teaser", "新Teaser"),
    AI_ANALYSIS_READY("ai_analysis_ready", "AI分析完成"),
    SYSTEM_ANNOUNCEMENT("system_announcement", "系统公告");

    private final String code;
    private final String description;
}
