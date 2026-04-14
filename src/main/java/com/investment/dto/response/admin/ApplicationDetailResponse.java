package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * BP申请详情响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailResponse {

    /**
     * 申请ID
     */
    private Long id;

    /**
     * 申请类型
     */
    private String applicationType;

    /**
     * 投资人用户ID
     */
    private Long investorUserId;

    /**
     * 投资人昵称
     */
    private String investorNickname;

    /**
     * 投资人手机号
     */
    private String investorPhone;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 融资方用户ID
     */
    private Long entrepreneurUserId;

    /**
     * 融资方昵称
     */
    private String entrepreneurNickname;

    /**
     * Teaser ID
     */
    private Long teaserId;

    /**
     * 联系信息
     */
    private String contactInfo;

    /**
     * 申请状态
     */
    private String status;

    /**
     * 申请理由
     */
    private String applicationReason;

    /**
     * 拒绝原因
     */
    private String rejectionReason;

    /**
     * 管理员备注
     */
    private String adminNotes;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private LocalDateTime reviewedAt;

    /**
     * 申请过期时间
     */
    private LocalDateTime expiresAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
