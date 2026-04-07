package com.investment.dto.response.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 最近活动响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityResponse {

    /**
     * 活动类型
     */
    private String type;

    /**
     * Teaser ID
     */
    private Long teaserId;

    /**
     * Teaser标题
     */
    private String teaserTitle;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动时间
     */
    private LocalDateTime timestamp;
}
