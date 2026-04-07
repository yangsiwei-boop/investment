package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.qa.AnswerRequest;
import com.investment.dto.response.qa.QaRecordResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.QaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 融资用户问答控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/entrepreneur/qa")
@RequiredArgsConstructor
@Tag(name = "融资用户-问答管理", description = "融资用户问答相关接口")
@SecurityRequirement(name = "Bearer")
public class EntrepreneurQaController {

    private final QaService qaService;

    /**
     * 获取收到的问答列表
     *
     * @param status    状态筛选
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 问答列表
     */
    @GetMapping
    @Operation(summary = "获取收到的问答列表", description = "获取融资用户收到的所有问答")
    public ApiResponse<Page<QaRecordResponse>> getQaRecords(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting QA records for entrepreneur: {}", principal.getId());

        Page<QaRecordResponse> records = qaService.getEntrepreneurQaRecords(principal.getId(), status, page, size);
        return ApiResponse.success(records);
    }

    /**
     * 回答问题
     *
     * @param qaId      问答ID
     * @param request   请求
     * @param principal 当前用户
     * @return 问答记录
     */
    @PostMapping("/{qaId}/answer")
    @Operation(summary = "回答问题", description = "回答投资人提出的问题")
    public ApiResponse<QaRecordResponse> answerQuestion(
            @PathVariable Long qaId,
            @Valid @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Answering question: {}, entrepreneur: {}", qaId, principal.getId());

        QaRecordResponse response = qaService.answerQuestion(qaId, principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取问答详情
     *
     * @param qaId      问答ID
     * @param principal 当前用户
     * @return 问答详情
     */
    @GetMapping("/{qaId}")
    @Operation(summary = "获取问答详情", description = "获取指定问答的详细信息")
    public ApiResponse<QaRecordResponse> getQaDetail(
            @PathVariable Long qaId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting QA detail: {}, entrepreneur: {}", qaId, principal.getId());

        QaRecordResponse response = qaService.getQaDetail(qaId, principal.getId());
        return ApiResponse.success(response);
    }
}
