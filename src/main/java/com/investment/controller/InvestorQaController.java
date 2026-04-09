package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.qa.QuestionSendRequest;
import com.investment.dto.request.qa.QaReplyRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 投资人问答控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/investor/qa")
@RequiredArgsConstructor
@Tag(name = "投资人-问答管理", description = "投资人问答相关接口")
@SecurityRequirement(name = "Bearer")
public class InvestorQaController {

    private final QaService qaService;

    /**
     * 发送问题
     *
     * @param request   请求
     * @param principal 当前用户
     * @return 问答记录
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "发送问题", description = "向项目方发送问题")
    public ApiResponse<QaRecordResponse> sendQuestion(
            @Valid @RequestBody QuestionSendRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Sending question from investor: {}", principal.getId());

        QaRecordResponse response = qaService.sendQuestion(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取我的问题列表
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 问题列表
     */
    @GetMapping
    @Operation(summary = "获取我的问题列表", description = "获取投资人发送的所有问题")
    public ApiResponse<Page<QaRecordResponse>> getMyQuestions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting questions for investor: {}", principal.getId());

        Page<QaRecordResponse> questions = qaService.getInvestorQuestions(principal.getId(), page, size);
        return ApiResponse.success(questions);
    }

    /**
     * 追问/追加消息
     *
     * @param qaId      问答ID
     * @param request   请求
     * @param principal 当前用户
     * @return 问答记录
     */
    @PostMapping("/{qaId}/reply")
    @Operation(summary = "追问/追加消息", description = "投资人追加问题或回复")
    public ApiResponse<QaRecordResponse> followUpQuestion(
            @PathVariable Long qaId,
            @Valid @RequestBody QaReplyRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Investor follow-up on question: {}, investor: {}", qaId, principal.getId());

        QaRecordResponse response = qaService.followUpQuestion(qaId, principal.getId(), request);
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
        log.info("Getting QA detail: {}, investor: {}", qaId, principal.getId());

        QaRecordResponse response = qaService.getQaDetail(qaId, principal.getId());
        return ApiResponse.success(response);
    }
}
