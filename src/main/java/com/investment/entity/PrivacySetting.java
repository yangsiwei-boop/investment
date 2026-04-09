package com.investment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * 隐私设置实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "privacy_settings")
public class PrivacySetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * 是否允许显示公司名称
     */
    @Column(name = "allow_show_company_name")
    @Builder.Default
    private Boolean allowShowCompanyName = false;

    /**
     * 是否允许显示成立时间
     */
    @Column(name = "allow_show_founded_time")
    @Builder.Default
    private Boolean allowShowFoundedTime = true;

    /**
     * 是否允许显示公司规模
     */
    @Column(name = "allow_show_company_scale")
    @Builder.Default
    private Boolean allowShowCompanyScale = true;

    /**
     * 是否允许显示办公地址
     */
    @Column(name = "allow_show_office_address")
    @Builder.Default
    private Boolean allowShowOfficeAddress = false;

    /**
     * 是否允许显示联系方式
     */
    @Column(name = "allow_show_contact_info")
    @Builder.Default
    private Boolean allowShowContactInfo = false;

    /**
     * 是否允许显示详细财务数据
     */
    @Column(name = "allow_show_financial_data")
    @Builder.Default
    private Boolean allowShowFinancialData = false;

    /**
     * 是否允许显示融资历史
     */
    @Column(name = "allow_show_financing_history")
    @Builder.Default
    private Boolean allowShowFinancingHistory = true;

    /**
     * 是否允许显示创始人详细信息
     */
    @Column(name = "allow_show_founder_details")
    @Builder.Default
    private Boolean allowShowFounderDetails = false;

    /**
     * 是否允许显示核心团队信息
     */
    @Column(name = "allow_show_team_info")
    @Builder.Default
    private Boolean allowShowTeamInfo = true;

    /**
     * 是否允许公开问答
     */
    @Column(name = "allow_public_qa")
    @Builder.Default
    private Boolean allowPublicQa = true;

    /**
     * 新回复默认是否公开
     */
    @Column(name = "default_qa_public")
    @Builder.Default
    private Boolean defaultQaPublic = true;

    /**
     * 问题库回复默认是否公开
     */
    @Column(name = "question_library_qa_public")
    @Builder.Default
    private Boolean questionLibraryQaPublic = true;

    /**
     * 是否需要审核才能查看完整BP
     */
    @Column(name = "require_bp_approval")
    @Builder.Default
    private Boolean requireBpApproval = true;

    /**
     * 是否需要审核才能获取联系方式
     */
    @Column(name = "require_contact_approval")
    @Builder.Default
    private Boolean requireContactApproval = true;

    /**
     * 是否允许投资人查看问答记录
     */
    @Column(name = "allow_view_qa_records")
    @Builder.Default
    private Boolean allowViewQaRecords = true;

    /**
     * 是否允许接收投资人提问
     */
    @Column(name = "allow_receive_questions")
    @Builder.Default
    private Boolean allowReceiveQuestions = true;

    /**
     * 自动回复模板
     */
    @Column(name = "auto_reply_template", columnDefinition = "TEXT")
    private String autoReplyTemplate;
}
