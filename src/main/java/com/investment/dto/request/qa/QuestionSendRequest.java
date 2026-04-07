package com.investment.dto.request.qa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 问题发送请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSendRequest {

    /**
     * Teaser ID
     */
    @NotNull(message = "Teaser ID不能为空")
    private Long teaserId;

    /**
     * 问题内容
     */
    @NotBlank(message = "问题内容不能为空")
    @Size(max = 2000, message = "问题内容不能超过2000个字符")
    private String question;

    /**
     * 是否公开
     */
    @Builder.Default
    private Boolean isPublic = true;

    /**
     * 问题分类
     */
    private String category;
}
