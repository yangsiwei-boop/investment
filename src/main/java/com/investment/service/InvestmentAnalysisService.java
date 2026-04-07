package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.investor.AnalysisRequest;
import com.investment.dto.response.investor.AnalysisResponse;
import com.investment.entity.InvestmentAnalysis;
import com.investment.entity.Teaser;
import com.investment.entity.User;
import com.investment.enums.TeaserStatus;
import com.investment.repository.InvestmentAnalysisRepository;
import com.investment.repository.TeaserRepository;
import com.investment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 投资分析服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentAnalysisService {

    private final InvestmentAnalysisRepository analysisRepository;
    private final TeaserRepository teaserRepository;
    private final UserRepository userRepository;

    /**
     * 创建投资分析
     *
     * @param investorId 投资人ID
     * @param request    分析请求
     * @return 分析结果
     */
    @Transactional
    public AnalysisResponse createAnalysis(Long investorId, AnalysisRequest request) {
        log.info("Creating analysis for investor: {}, teaser: {}", investorId, request.getTeaserId());

        // 检查Teaser是否存在
        Teaser teaser = teaserRepository.findById(request.getTeaserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        if (teaser.getStatus() != TeaserStatus.PUBLISHED) {
            throw new BusinessException(ErrorCode.TEASER_NOT_AVAILABLE);
        }

        // 获取投资人
        User investor = userRepository.findById(investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 检查是否已有分析
        if (analysisRepository.findByTeaserIdAndInvestorUserId(request.getTeaserId(), investorId).isPresent()) {
            throw new BusinessException(ErrorCode.ANALYSIS_ALREADY_EXISTS);
        }

        // 创建分析记录
        InvestmentAnalysis analysis = InvestmentAnalysis.builder()
                .teaser(teaser)
                .investorUser(investor)
                .isAiGenerated(true)
                .analysisType("basic")
                .build();

        analysisRepository.save(analysis);

        // 模拟AI分析（实际项目中应该调用AI服务）
        performMockAnalysis(analysis);

        return convertToResponse(analysis);
    }

    /**
     * 获取分析详情
     *
     * @param analysisId 分析ID
     * @param investorId 投资人ID
     * @return 分析详情
     */
    public AnalysisResponse getAnalysis(Long analysisId, Long investorId) {
        InvestmentAnalysis analysis = analysisRepository.findById(analysisId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 验证权限
        if (!analysis.getInvestorUser().getId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        return convertToResponse(analysis);
    }

    /**
     * 获取投资人的分析列表
     *
     * @param investorId 投资人ID
     * @param page       页码
     * @param size       每页数量
     * @return 分析列表
     */
    public Page<AnalysisResponse> getAnalysisList(Long investorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<InvestmentAnalysis> analysisPage = analysisRepository.findByInvestorUserIdOrderByCreatedAtDesc(investorId, pageable);

        return analysisPage.map(this::convertToResponse);
    }

    /**
     * 获取某Teaser的分析
     *
     * @param teaserId   Teaser ID
     * @param investorId 投资人ID
     * @return 分析结果
     */
    public AnalysisResponse getAnalysisByTeaser(Long teaserId, Long investorId) {
        InvestmentAnalysis analysis = analysisRepository.findByTeaserIdAndInvestorUserId(teaserId, investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANALYSIS_NOT_FOUND));

        return convertToResponse(analysis);
    }

    /**
     * 重新分析
     *
     * @param analysisId 分析ID
     * @param investorId 投资人ID
     * @return 分析结果
     */
    @Transactional
    public AnalysisResponse reanalyze(Long analysisId, Long investorId) {
        InvestmentAnalysis analysis = analysisRepository.findById(analysisId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 验证权限
        if (!analysis.getInvestorUser().getId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 模拟AI分析
        performMockAnalysis(analysis);

        return convertToResponse(analysis);
    }

    /**
     * 删除分析
     *
     * @param analysisId 分析ID
     * @param investorId 投资人ID
     */
    @Transactional
    public void deleteAnalysis(Long analysisId, Long investorId) {
        InvestmentAnalysis analysis = analysisRepository.findById(analysisId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 验证权限
        if (!analysis.getInvestorUser().getId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        analysisRepository.delete(analysis);
    }

    /**
     * 模拟AI分析
     */
    private void performMockAnalysis(InvestmentAnalysis analysis) {
        // 模拟分析过程（实际项目中应该调用AI服务）
        Random random = new Random();

        // 生成随机评分
        int overallScore = 60 + random.nextInt(30);

        // 生成评分等级
        String overallVerdict;
        if (overallScore >= 85) {
            overallVerdict = "excellent";
        } else if (overallScore >= 70) {
            overallVerdict = "good";
        } else if (overallScore >= 55) {
            overallVerdict = "average";
        } else if (overallScore >= 40) {
            overallVerdict = "below_average";
        } else {
            overallVerdict = "poor";
        }

        // 更新分析结果
        analysis.setOverallScore(overallScore);
        analysis.setOverallVerdict(overallVerdict);
        analysis.setIndustryAnalysisScore(60 + random.nextInt(30));
        analysis.setIndustryAnalysisText("行业市场空间广阔，具有良好的发展前景。市场规模持续扩大，行业竞争格局相对稳定。");
        analysis.setTeamAnalysisScore(60 + random.nextInt(30));
        analysis.setTeamAnalysisText("创始团队经验丰富，核心成员具有相关行业背景。团队执行力强，组织架构清晰。");
        analysis.setTechnologyAnalysisScore(60 + random.nextInt(30));
        analysis.setTechnologyAnalysisText("技术方案具有创新性，核心技术有一定壁垒。研发投入持续，技术迭代能力强。");
        analysis.setCompetitivenessAnalysisScore(60 + random.nextInt(30));
        analysis.setCompetitivenessAnalysisText("产品差异化明显，具有一定的竞争优势。市场定位清晰，客户粘性较强。");
        analysis.setFinancialHealthScore(60 + random.nextInt(30));
        analysis.setFinancialHealthText("财务数据健康，营收增长稳定。成本控制良好，盈利能力逐步提升。");
        analysis.setInvestmentValueScore(60 + random.nextInt(30));
        analysis.setInvestmentValueText("具有较高的投资价值，建议重点关注。风险可控，预期回报率较好。");

        // 投资亮点
        analysis.setInvestmentHighlights("1. 市场空间广阔，增长潜力大\n2. 团队背景优秀，执行力强\n3. 产品差异化明显，具有竞争优势\n4. 商业模式清晰，盈利路径明确");

        // 风险提示
        analysis.setRiskWarnings("1. 市场竞争激烈，需要持续创新\n2. 行业监管政策存在不确定性\n3. 规模化扩张需要大量资金支持");

        // 投资建议
        analysis.setInvestmentSuggestion(generateInvestmentSuggestion(overallScore, overallVerdict));

        // 市场数据
        analysis.setMarketSize("100亿+");
        analysis.setMarketGrowthRate("15%");

        analysisRepository.save(analysis);
    }

    /**
     * 生成投资建议
     */
    private String generateInvestmentSuggestion(int overallScore, String verdict) {
        return switch (verdict) {
            case "excellent" -> "综合评分优秀，建议重点关注，可以考虑深入尽调和投资";
            case "good" -> "综合评分良好，建议进行初步尽调，评估投资机会";
            case "average" -> "综合评分一般，建议观望，等待更多信息后再做决策";
            case "below_average" -> "综合评分偏低，存在较多风险，建议谨慎考虑";
            default -> "综合评分较低，不建议投资";
        };
    }

    /**
     * 转换为响应DTO
     */
    private AnalysisResponse convertToResponse(InvestmentAnalysis analysis) {
        Teaser teaser = analysis.getTeaser();

        // 构建风险评估
        Map<String, Object> riskAssessment = new HashMap<>();
        int riskScore = 100 - (analysis.getOverallScore() != null ? analysis.getOverallScore() : 50);
        riskAssessment.put("riskLevel", riskScore <= 30 ? "low" : (riskScore <= 50 ? "medium" : "high"));
        riskAssessment.put("riskScore", riskScore);
        riskAssessment.put("marketRisk", "中等风险");
        riskAssessment.put("competitiveRisk", "中等风险");
        riskAssessment.put("financialRisk", "低风险");
        riskAssessment.put("teamRisk", "低风险");

        return AnalysisResponse.builder()
                .id(analysis.getId())
                .teaserId(teaser != null ? teaser.getId() : null)
                .teaserTitle(teaser != null ? teaser.getTitle() : null)
                .status(analysis.getOverallScore() != null ? "completed" : "processing")
                .overallScore(analysis.getOverallScore())
                .scoreGrade(analysis.getOverallVerdict())
                .marketScore(analysis.getIndustryAnalysisScore())
                .teamScore(analysis.getTeamAnalysisScore())
                .productScore(analysis.getTechnologyAnalysisScore())
                .businessModelScore(analysis.getCompetitivenessAnalysisScore())
                .financialScore(analysis.getFinancialHealthScore())
                .competitivenessScore(analysis.getInvestmentValueScore())
                .riskAssessment(riskAssessment)
                .highlights(analysis.getInvestmentHighlights())
                .risks(analysis.getRiskWarnings())
                .detailedAnalysis(buildDetailedAnalysis(analysis))
                .investmentAdvice(analysis.getInvestmentSuggestion())
                .createdAt(analysis.getCreatedAt())
                .build();
    }

    /**
     * 构建详细分析Map
     */
    private Map<String, Object> buildDetailedAnalysis(InvestmentAnalysis analysis) {
        Map<String, Object> detailedAnalysis = new HashMap<>();

        if (analysis.getIndustryAnalysisScore() != null) {
            detailedAnalysis.put("marketAnalysis", Map.of(
                    "score", analysis.getIndustryAnalysisScore(),
                    "comment", analysis.getIndustryAnalysisText() != null ? analysis.getIndustryAnalysisText() : ""
            ));
        }

        if (analysis.getTeamAnalysisScore() != null) {
            detailedAnalysis.put("teamAnalysis", Map.of(
                    "score", analysis.getTeamAnalysisScore(),
                    "comment", analysis.getTeamAnalysisText() != null ? analysis.getTeamAnalysisText() : ""
            ));
        }

        if (analysis.getTechnologyAnalysisScore() != null) {
            detailedAnalysis.put("productAnalysis", Map.of(
                    "score", analysis.getTechnologyAnalysisScore(),
                    "comment", analysis.getTechnologyAnalysisText() != null ? analysis.getTechnologyAnalysisText() : ""
            ));
        }

        if (analysis.getCompetitivenessAnalysisScore() != null) {
            detailedAnalysis.put("businessModelAnalysis", Map.of(
                    "score", analysis.getCompetitivenessAnalysisScore(),
                    "comment", analysis.getCompetitivenessAnalysisText() != null ? analysis.getCompetitivenessAnalysisText() : ""
            ));
        }

        if (analysis.getFinancialHealthScore() != null) {
            detailedAnalysis.put("financialAnalysis", Map.of(
                    "score", analysis.getFinancialHealthScore(),
                    "comment", analysis.getFinancialHealthText() != null ? analysis.getFinancialHealthText() : ""
            ));
        }

        if (analysis.getInvestmentValueScore() != null) {
            detailedAnalysis.put("competitivenessAnalysis", Map.of(
                    "score", analysis.getInvestmentValueScore(),
                    "comment", analysis.getInvestmentValueText() != null ? analysis.getInvestmentValueText() : ""
            ));
        }

        return detailedAnalysis;
    }
}
