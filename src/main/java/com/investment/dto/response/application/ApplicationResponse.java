package com.investment.dto.response.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 申请响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    /**
     * 申请ID
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
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 申请类型
     */
    private String applicationType;

    /**
     * 申请理由
     */
    private String reason;

    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 职位
     */
    private String position;

    /**
     * 申请状态
     */
    private String status;

    /**
     * 审核意见
     */
    private String reviewComment;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private LocalDateTime reviewedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
