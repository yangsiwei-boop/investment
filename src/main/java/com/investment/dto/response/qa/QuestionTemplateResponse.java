package com.investment.dto.response.qa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 问题模板响应DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTemplateResponse {

    /**
     * 问题ID
     */
    private Long id;

    /**
     * 问题内容
     */
    private String question;

    /**
     * 问题分类
     */
    private String category;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
