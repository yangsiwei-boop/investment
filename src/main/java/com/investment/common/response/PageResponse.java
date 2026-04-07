package com.investment.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页响应封装类
 *
 * @param <T> 数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 从Spring Data Page转换
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .total(page.getTotalElements())
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .list(page.getContent())
                .build();
    }

    /**
     * 手动构建分页响应
     */
    public static <T> PageResponse<T> of(List<T> list, Long total, Integer page, Integer size) {
        int totalPages = (int) Math.ceil((double) total / size);
        return PageResponse.<T>builder()
                .total(total)
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .list(list)
                .build();
    }
}
