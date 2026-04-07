package com.investment.dto.response.entrepreneur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 融资用户工作台统计响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurDashboardResponse {

    /**
     * 项目总数
     */
    private Long projectCount;

    /**
     * 已发布Teaser数量
     */
    private Long publishedTeaserCount;

    /**
     * 待处理申请数量
     */
    private Long pendingApplicationCount;

    /**
     * 总浏览次数
     */
    private Long totalViewCount;

    /**
     * 总收藏次数
     */
    private Long totalFavoriteCount;

    /**
     * BP下载次数
     */
    private Long bpDownloadCount;

    /**
     * 待回复问题数量
     */
    private Long pendingQuestionCount;
}
