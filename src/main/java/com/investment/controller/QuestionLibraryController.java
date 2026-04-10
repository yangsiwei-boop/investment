package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.qa.QuestionTemplateResponse;
import com.investment.entity.InvestorQuestion;
import com.investment.enums.QuestionCategory;
import com.investment.security.UserPrincipal;
import com.investment.service.InvestorQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问题库控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/question-library")
@RequiredArgsConstructor
@Tag(name = "问题库", description = "问题模板和问题库相关接口")
@SecurityRequirement(name = "Bearer")
public class QuestionLibraryController {

    private final InvestorQuestionService investorQuestionService;

    /**
     * 获取所有问题模板
     *
     * @return 问题模板列表
     */
    @GetMapping
    @Operation(summary = "获取问题库列表", description = "获取所有问题模板列表")
    public ApiResponse<List<QuestionTemplateResponse>> getQuestionLibrary(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting question library for user: {}", principal.getId());

        List<InvestorQuestion> questions = investorQuestionService.getAllQuestions();
        List<QuestionTemplateResponse> responses = questions.stream()
                .map(this::convertToResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    /**
     * 获取问题模板列表（仅模板）
     *
     * @param category 分类筛选（可选）
     * @return 问题模板列表
     */
    @GetMapping("/templates")
    @Operation(summary = "获取问题模板", description = "获取平台预设的问题模板，支持按分类筛选")
    public ApiResponse<List<QuestionTemplateResponse>> getTemplates(
            @RequestParam(required = false) String category,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting question templates, category: {}", category);

        List<InvestorQuestion> questions;
        if (category != null && !category.isEmpty()) {
            questions = investorQuestionService.getQuestionsByCategory(
                    QuestionCategory.valueOf(category));
        } else {
            questions = investorQuestionService.getAllQuestions();
        }

        List<QuestionTemplateResponse> responses = questions.stream()
                .map(this::convertToResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    /**
     * 获取热门问题
     *
     * @param limit 数量限制
     * @return 热门问题列表
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门问题", description = "获取使用次数最多的问题")
    public ApiResponse<List<QuestionTemplateResponse>> getHotQuestions(
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting hot questions, limit: {}", limit);

        List<InvestorQuestion> questions = investorQuestionService.getHotQuestions(limit);
        List<QuestionTemplateResponse> responses = questions.stream()
                .map(this::convertToResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    private QuestionTemplateResponse convertToResponse(InvestorQuestion q) {
        return QuestionTemplateResponse.builder()
                .id(q.getId())
                .question(q.getQuestionTitle())
                .category(q.getCategory() != null ? q.getCategory().name() : null)
                .sortOrder(q.getSortOrder())
                .usageCount(q.getUsageCount())
                .createdAt(q.getCreatedAt())
                .build();
    }
}
