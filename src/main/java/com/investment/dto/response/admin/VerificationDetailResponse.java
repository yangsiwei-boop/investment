package com.investment.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实名认证详情响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDetailResponse {

    /**
     * 认证ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCardNumber;

    /**
     * 身份证正面照
     */
    private String idCardFrontUrl;

    /**
     * 身份证反面照
     */
    private String idCardBackUrl;

    /**
     * 认证类型
     */
    private String verificationType;

    /**
     * 认证状态
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
