package com.investment.entity;

import com.investment.enums.QuestionCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 投资人问题库实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investor_questions")
public class InvestorQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 投资人用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_user_id", nullable = false)
    private User investorUser;

    /**
     * 问题标题
     */
    @Column(name = "question_title", nullable = false, length = 255)
    private String questionTitle;

    /**
     * 问题描述
     */
    @Column(name = "question_description", columnDefinition = "TEXT")
    private String questionDescription;

    /**
     * 问题分类
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private QuestionCategory category;

    /**
     * 问题标签（多个标签用逗号分隔）
     */
    @Column(name = "question_tags", length = 255)
    private String questionTags;

    /**
     * 是否常用问题
     */
    @Column(name = "is_frequent")
    @Builder.Default
    private Boolean isFrequent = false;

    /**
     * 是否为模板问题（平台预设）
     */
    @Column(name = "is_template")
    @Builder.Default
    private Boolean isTemplate = false;

    /**
     * 使用次数
     */
    @Column(name = "usage_count")
    @Builder.Default
    private Integer usageCount = 0;

    /**
     * 最后使用时间
     */
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;
}
