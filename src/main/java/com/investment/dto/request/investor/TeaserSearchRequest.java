package com.investment.dto.request.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Teaser搜索请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeaserSearchRequest {

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 行业类型列表
     */
    private List<String> industries;

    /**
     * 融资阶段列表
     */
    private List<String> financingStages;

    /**
     * 最小融资金额（万元）
     */
    private BigDecimal minFinancingAmount;

    /**
     * 最大融资金额（万元）
     */
    private BigDecimal maxFinancingAmount;

    /**
     * 地区
     */
    private String region;

    /**
     * 排序字段（created_at, view_count, match_score）
     */
    @Builder.Default
    private String sortBy = "created_at";

    /**
     * 排序方向（asc, desc）
     */
    @Builder.Default
    private String sortOrder = "desc";

    /**
     * 页码
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 每页数量
     */
    @Builder.Default
    private Integer pageSize = 20;
}
