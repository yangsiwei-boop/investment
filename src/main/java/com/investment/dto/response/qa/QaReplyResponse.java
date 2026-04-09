package com.investment.dto.response.qa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 问答回复响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaReplyResponse {

    /**
     * 回复ID
     */
    private Long id;

    /**
     * 关联的问答记录ID
     */
    private Long qaRecordId;

    /**
     * 回复者ID
     */
    private Long userId;

    /**
     * 回复者名称
     */
    private String userName;

    /**
     * 回复者类型（INVESTOR/ENTREPRENEUR）
     */
    private String userType;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 回复时间
     */
    private LocalDateTime createdAt;
}
