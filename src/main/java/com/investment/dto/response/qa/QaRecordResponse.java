package com.investment.dto.response.qa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 问答记录响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaRecordResponse {

    /**
     * 问答ID
     */
    private Long id;

    /**
     * Teaser ID
     */
    private Long teaserId;

    /**
     * Teaser标题
     */
    private String teaserTitle;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 提问者ID
     */
    private Long questionerId;

    /**
     * 提问者名称
     */
    private String questionerName;

    /**
     * 问题内容
     */
    private String question;

    /**
     * 问题分类
     */
    private String category;

    /**
     * 回答内容（最新一条回复的快捷字段，兼容旧前端）
     */
    private String answer;

    /**
     * 回答者ID
     */
    private Long answererId;

    /**
     * 回答者名称
     */
    private String answererName;

    /**
     * 问答状态
     */
    private String status;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 提问时间
     */
    private LocalDateTime questionedAt;

    /**
     * 回答时间
     */
    private LocalDateTime answeredAt;

    /**
     * 回复数量
     */
    private Integer replyCount;

    /**
     * 所有回复列表（按时间升序）
     */
    private List<QaReplyResponse> replies;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
