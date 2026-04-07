package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.investor.AnalysisRequest;
import com.investment.dto.response.investor.AnalysisResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.InvestmentAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 投资人分析控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/investor/analysis")
@RequiredArgsConstructor
@Tag(name = "投资人-投资分析", description = "投资人投资分析相关接口")
@SecurityRequirement(name = "Bearer")
public class InvestorAnalysisController {

    private final InvestmentAnalysisService analysisService;

    /**
     * 创建投资分析
     *
     * @param request   分析请求
     * @param principal 当前用户
     * @return 分析结果
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建投资分析", description = "为指定Teaser创建AI投资分析")
    public ApiResponse<AnalysisResponse> createAnalysis(
            @RequestBody AnalysisRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Creating analysis for investor: {}, teaser: {}", principal.getId(), request.getTeaserId());

        AnalysisResponse response = analysisService.createAnalysis(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取分析详情
     *
     * @param analysisId 分析ID
     * @param principal  当前用户
     * @return 分析详情
     */
    @GetMapping("/{analysisId}")
    @Operation(summary = "获取分析详情", description = "获取指定投资分析的详细信息")
    public ApiResponse<AnalysisResponse> getAnalysis(
            @PathVariable Long analysisId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting analysis: {}, investor: {}", analysisId, principal.getId());

        AnalysisResponse response = analysisService.getAnalysis(analysisId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 获取分析列表
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 分析列表
     */
    @GetMapping
    @Operation(summary = "获取分析列表", description = "获取投资人的所有投资分析列表")
    public ApiResponse<Page<AnalysisResponse>> getAnalysisList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting analysis list for investor: {}", principal.getId());

        Page<AnalysisResponse> analysisPage = analysisService.getAnalysisList(principal.getId(), page, size);
        return ApiResponse.success(analysisPage);
    }

    /**
     * 获取某Teaser的分析
     *
     * @param teaserId  Teaser ID
     * @param principal 当前用户
     * @return 分析结果
     */
    @GetMapping("/teaser/{teaserId}")
    @Operation(summary = "获取Teaser的分析", description = "获取指定Teaser的投资分析")
    public ApiResponse<AnalysisResponse> getAnalysisByTeaser(
            @PathVariable Long teaserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting analysis for teaser: {}, investor: {}", teaserId, principal.getId());

        AnalysisResponse response = analysisService.getAnalysisByTeaser(teaserId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 重新分析
     *
     * @param analysisId 分析ID
     * @param principal  当前用户
     * @return 分析结果
     */
    @PostMapping("/{analysisId}/reanalyze")
    @Operation(summary = "重新分析", description = "重新执行投资分析")
    public ApiResponse<AnalysisResponse> reanalyze(
            @PathVariable Long analysisId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Reanalyzing: {}, investor: {}", analysisId, principal.getId());

        AnalysisResponse response = analysisService.reanalyze(analysisId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 删除分析
     *
     * @param analysisId 分析ID
     * @param principal  当前用户
     * @return 成功响应
     */
    @DeleteMapping("/{analysisId}")
    @Operation(summary = "删除分析", description = "删除指定的投资分析")
    public ApiResponse<Void> deleteAnalysis(
            @PathVariable Long analysisId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Deleting analysis: {}, investor: {}", analysisId, principal.getId());

        analysisService.deleteAnalysis(analysisId, principal.getId());
        return ApiResponse.success(null);
    }
}
