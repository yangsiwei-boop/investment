package com.investment.dto.request.investor;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI分析请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    /**
     * Teaser ID
     */
    @NotNull(message = "Teaser ID不能为空")
    private Long teaserId;

    /**
     * 分析类型
     */
    @Builder.Default
    private String analysisType = "comprehensive";

    /**
     * 是否深度分析
     */
    @Builder.Default
    private Boolean deepAnalysis = false;

    /**
     * 自定义分析维度
     */
    private String customDimensions;
}
