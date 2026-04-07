package com.investment.dto.request.qa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 问题回复请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    /**
     * 回答内容
     */
    @NotBlank(message = "回答内容不能为空")
    @Size(max = 5000, message = "回答内容不能超过5000个字符")
    private String answer;

    /**
     * 是否公开
     */
    @Builder.Default
    private Boolean isPublic = true;
}
