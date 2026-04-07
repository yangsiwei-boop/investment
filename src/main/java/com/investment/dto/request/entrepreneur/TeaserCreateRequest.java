package com.investment.dto.request.entrepreneur;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Teaser创建请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeaserCreateRequest {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题不能超过100个字符")
    private String title;

    /**
     * 一句话介绍
     */
    @NotBlank(message = "一句话介绍不能为空")
    @Size(max = 200, message = "一句话介绍不能超过200个字符")
    private String summary;

    /**
     * 图标emoji
     */
    private String iconEmoji;

    /**
     * 核心亮点（JSON数组）
     */
    private List<String> highlights;

    /**
     * 商业模式
     */
    @Size(max = 2000, message = "商业模式不能超过2000个字符")
    private String businessModel;

    /**
     * 目标市场
     */
    @Size(max = 1000, message = "目标市场不能超过1000个字符")
    private String targetMarket;

    /**
     * 竞争优势
     */
    @Size(max = 2000, message = "竞争优势不能超过2000个字符")
    private String competitiveAdvantage;

    /**
     * 团队介绍
     */
    @Size(max = 2000, message = "团队介绍不能超过2000个字符")
    private String teamIntroduction;

    /**
     * 是否自动生成
     */
    @Builder.Default
    private Boolean autoGenerate = false;
}
