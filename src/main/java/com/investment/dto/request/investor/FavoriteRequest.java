package com.investment.dto.request.investor;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏请求DTO
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {

    /**
     * Teaser ID
     */
    @NotNull(message = "Teaser ID不能为空")
    private Long teaserId;

    /**
     * 收藏分组（可选）
     */
    private String groupName;

    /**
     * 备注
     */
    private String note;
}
