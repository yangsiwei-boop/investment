package com.investment.dto.request.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 隐私设置更新请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettingUpdateRequest {

    /**
     * 是否允许显示公司名称
     */
    private Boolean allowShowCompanyName;

    /**
     * 是否允许显示成立时间
     */
    private Boolean allowShowFoundedTime;

    /**
     * 是否允许显示办公地址
     */
    private Boolean allowShowOfficeAddress;

    /**
     * 是否允许显示联系方式
     */
    private Boolean allowShowContactInfo;

    /**
     * 是否允许显示详细财务数据
     */
    private Boolean allowShowFinancialData;

    /**
     * 是否允许显示融资历史
     */
    private Boolean allowShowFinancingHistory;

    /**
     * 是否允许显示创始人详细信息
     */
    private Boolean allowShowFounderDetails;

    /**
     * 是否允许显示核心团队信息
     */
    private Boolean allowShowTeamInfo;

    /**
     * 是否允许公开问答
     */
    private Boolean allowPublicQa;

    /**
     * 默认问答是否公开
     */
    private Boolean defaultQaPublic;

    /**
     * 问题库回复默认是否公开
     */
    private Boolean questionLibraryQaPublic;

    /**
     * 是否需要审核才能查看完整BP
     */
    private Boolean requireBpApproval;

    /**
     * 是否需要审核才能获取联系方式
     */
    private Boolean requireContactApproval;

    /**
     * 是否允许投资人查看问答记录
     */
    private Boolean allowViewQaRecords;

    /**
     * 是否允许接收投资人提问
     */
    private Boolean allowReceiveQuestions;

    /**
     * 自动回复模板
     */
    private String autoReplyTemplate;
}
