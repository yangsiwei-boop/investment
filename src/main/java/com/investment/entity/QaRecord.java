package com.investment.entity;

import com.investment.enums.QuestionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 问答记录实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "qa_records")
public class QaRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 项目ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    /**
     * 投资人用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_user_id", nullable = false)
    private User investorUser;

    /**
     * 融资用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepreneur_user_id", nullable = false)
    private User entrepreneurUser;

    /**
     * 问题内容
     */
    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    /**
     * 投资人留言
     */
    @Column(name = "investor_message", columnDefinition = "TEXT")
    private String investorMessage;

    /**
     * 回答内容
     */
    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    /**
     * 草稿回答内容
     */
    @Column(name = "draft_answer", columnDefinition = "TEXT")
    private String draftAnswer;

    /**
     * 是否公开
     */
    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = false;

    /**
     * 是否来自问题库
     */
    @Column(name = "is_from_question_library")
    @Builder.Default
    private Boolean isFromQuestionLibrary = false;

    /**
     * 问题库问题ID
     */
    @Column(name = "question_library_id")
    private Long questionLibraryId;

    /**
     * 融资方是否允许公开此问答
     */
    @Column(name = "allow_public")
    @Builder.Default
    private Boolean allowPublic = true;

    /**
     * 是否使用隐私设置
     */
    @Column(name = "use_privacy_setting")
    @Builder.Default
    private Boolean usePrivacySetting = true;

    /**
     * 问题状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "question_status", length = 20)
    @Builder.Default
    private QuestionStatus questionStatus = QuestionStatus.PENDING;

    /**
     * 发送时间
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    /**
     * 回答时间
     */
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    /**
     * 投资人查看回复的时间
     */
    @Column(name = "investor_viewed_at")
    private LocalDateTime investorViewedAt;

    /**
     * 融资方查看问题的时间
     */
    @Column(name = "entrepreneur_viewed_at")
    private LocalDateTime entrepreneurViewedAt;

    /**
     * 覆盖父类的createdAt和updatedAt字段，数据库表中不存在这些列
     */
    @Transient
    private LocalDateTime createdAt;

    @Transient
    private LocalDateTime updatedAt;
}
