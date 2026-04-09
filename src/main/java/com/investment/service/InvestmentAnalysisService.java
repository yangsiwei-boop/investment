package com.investment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.investor.AnalysisRequest;
import com.investment.dto.response.investor.AnalysisResponse;
import com.investment.entity.InvestmentAnalysis;
import com.investment.entity.Teaser;
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

import java.math.BigDecimal;
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
    private final ObjectMapper objectMapper;

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

        // 检查用户是否存在
        if (!userRepository.existsById(investorId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 检查是否已有分析
        if (analysisRepository.findByTeaserIdAndInvestorUserId(request.getTeaserId(), investorId).isPresent()) {
            throw new BusinessException(ErrorCode.ANALYSIS_ALREADY_EXISTS);
        }

        // 创建分析记录
        InvestmentAnalysis analysis = InvestmentAnalysis.builder()
                .teaserId(request.getTeaserId())
                .investorUserId(investorId)
                .analysisType("basic")
                .build();

        analysisRepository.save(analysis);

        // 模拟AI分析（实际项目中应该调用AI服务）
        performMockAnalysis(analysis);

        return convertToResponse(analysis, teaser);
    }

    /**
     * 获取分析详情
     *
     * @param analysisId 分析ID
     * @param investorId 投资人ID
     * @return 分析详情
     */
    @Transactional(readOnly = true)
    public AnalysisResponse getAnalysis(Long analysisId, Long investorId) {
        InvestmentAnalysis analysis = analysisRepository.findById(analysisId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 验证权限
        if (!analysis.getInvestorUserId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        Teaser teaser = teaserRepository.findById(analysis.getTeaserId()).orElse(null);
        return convertToResponse(analysis, teaser);
    }

    /**
     * 获取投资人的分析列表
     *
     * @param investorId 投资人ID
     * @param page       页码
     * @param size       每页数量
     * @return 分析列表
     */
    @Transactional(readOnly = true)
    public Page<AnalysisResponse> getAnalysisList(Long investorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<InvestmentAnalysis> analysisPage = analysisRepository.findByInvestorUserIdOrderByCreatedAtDesc(investorId, pageable);

        return analysisPage.map(analysis -> {
            Teaser teaser = teaserRepository.findById(analysis.getTeaserId()).orElse(null);
            return convertToResponse(analysis, teaser);
        });
    }

    /**
     * 获取某Teaser的分析
     *
     * @param teaserId   Teaser ID
     * @param investorId 投资人ID
     * @return 分析结果
     */
    @Transactional(readOnly = true)
    public AnalysisResponse getAnalysisByTeaser(Long teaserId, Long investorId) {
        InvestmentAnalysis analysis = analysisRepository.findByTeaserIdAndInvestorUserId(teaserId, investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANALYSIS_NOT_FOUND));

        Teaser teaser = teaserRepository.findById(teaserId).orElse(null);
        return convertToResponse(analysis, teaser);
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
        if (!analysis.getInvestorUserId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 模拟AI分析
        performMockAnalysis(analysis);

        Teaser teaser = teaserRepository.findById(analysis.getTeaserId()).orElse(null);
        return convertToResponse(analysis, teaser);
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
        if (!analysis.getInvestorUserId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        analysisRepository.delete(analysis);
    }

    /**
     * 模拟AI分析
     */
    private void performMockAnalysis(InvestmentAnalysis analysis) {
        Random random = new Random();

        // 生成随机评分
        double score = 60 + random.nextInt(30);

        // 生成评分等级
        String recommendation;
        if (score >= 85) {
            recommendation = "excellent";
        } else if (score >= 70) {
            recommendation = "good";
        } else if (score >= 55) {
            recommendation = "average";
        } else if (score >= 40) {
            recommendation = "below_average";
        } else {
            recommendation = "poor";
        }

        // 构建详细分析内容
        Map<String, Object> analysisContent = new HashMap<>();
        analysisContent.put("overallScore", (int) score);
        analysisContent.put("overallVerdict", recommendation);

        // 各维度分析
        analysisContent.put("industryAnalysis", Map.of(
                "score", 60 + random.nextInt(30),
                "text", "行业市场空间广阔，具有良好的发展前景。"
        ));
        analysisContent.put("teamAnalysis", Map.of(
                "score", 60 + random.nextInt(30),
                "text", "创始团队经验丰富，核心成员具有相关行业背景。"
        ));
        analysisContent.put("technologyAnalysis", Map.of(
                "score", 60 + random.nextInt(30),
                "text", "技术方案具有创新性，核心技术有一定壁垒。"
        ));
        analysisContent.put("competitivenessAnalysis", Map.of(
                "score", 60 + random.nextInt(30),
                "text", "产品差异化明显，具有一定的竞争优势。"
        ));
        analysisContent.put("financialHealth", Map.of(
                "score", 60 + random.nextInt(30),
                "text", "财务数据健康，营收增长稳定。"
        ));
        analysisContent.put("investmentValue", Map.of(
                "score", 60 + random.nextInt(30),
                "text", "具有较高的投资价值，建议重点关注。"
        ));

        // 投资亮点和风险
        analysisContent.put("investmentHighlights",
                "1. 市场空间广阔，增长潜力大\n2. 团队背景优秀，执行力强\n3. 产品差异化明显");
        analysisContent.put("riskWarnings",
                "1. 市场竞争激烈\n2. 行业监管政策存在不确定性\n3. 规模化扩张需要大量资金");
        analysisContent.put("investmentSuggestion", generateInvestmentSuggestion((int) score, recommendation));

        // 转换为JSON字符串存储
        try {
            analysis.setAnalysisContent(objectMapper.writeValueAsString(analysisContent));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize analysis content", e);
            analysis.setAnalysisContent("{}");
        }

        analysis.setScore(BigDecimal.valueOf(score));
        analysis.setRecommendation(recommendation);

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
    private AnalysisResponse convertToResponse(InvestmentAnalysis analysis, Teaser teaser) {
        // 解析analysisContent
        Map<String, Object> content = parseAnalysisContent(analysis.getAnalysisContent());

        // 构建风险评估
        Map<String, Object> riskAssessment = new HashMap<>();
        int riskScore = 100 - (analysis.getScore() != null ? analysis.getScore().intValue() : 50);
        riskAssessment.put("riskLevel", riskScore <= 30 ? "low" : (riskScore <= 50 ? "medium" : "high"));
        riskAssessment.put("riskScore", riskScore);
        riskAssessment.put("marketRisk", "中等风险");
        riskAssessment.put("competitiveRisk", "中等风险");
        riskAssessment.put("financialRisk", "低风险");
        riskAssessment.put("teamRisk", "低风险");

        // 从content中提取各维度分数
        Integer marketScore = getScoreFromContent(content, "industryAnalysis");
        Integer teamScore = getScoreFromContent(content, "teamAnalysis");
        Integer productScore = getScoreFromContent(content, "technologyAnalysis");
        Integer businessModelScore = getScoreFromContent(content, "competitivenessAnalysis");
        Integer financialScore = getScoreFromContent(content, "financialHealth");
        Integer competitivenessScore = getScoreFromContent(content, "investmentValue");

        return AnalysisResponse.builder()
                .id(analysis.getId())
                .teaserId(analysis.getTeaserId())
                .teaserTitle(teaser != null ? teaser.getTitle() : null)
                .status(analysis.getScore() != null ? "completed" : "processing")
                .overallScore(analysis.getScore() != null ? analysis.getScore().intValue() : null)
                .scoreGrade(analysis.getRecommendation())
                .marketScore(marketScore)
                .teamScore(teamScore)
                .productScore(productScore)
                .businessModelScore(businessModelScore)
                .financialScore(financialScore)
                .competitivenessScore(competitivenessScore)
                .riskAssessment(riskAssessment)
                .highlights(getStringFromContent(content, "investmentHighlights"))
                .risks(getStringFromContent(content, "riskWarnings"))
                .detailedAnalysis(buildDetailedAnalysis(content))
                .investmentAdvice(getStringFromContent(content, "investmentSuggestion"))
                .createdAt(analysis.getCreatedAt())
                .build();
    }

    /**
     * 解析分析内容JSON
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseAnalysisContent(String jsonContent) {
        if (jsonContent == null || jsonContent.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(jsonContent, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse analysis content", e);
            return new HashMap<>();
        }
    }

    /**
     * 从内容中获取分数
     */
    @SuppressWarnings("unchecked")
    private Integer getScoreFromContent(Map<String, Object> content, String key) {
        Object value = content.get(key);
        if (value instanceof Map) {
            Object score = ((Map<String, Object>) value).get("score");
            if (score instanceof Number) {
                return ((Number) score).intValue();
            }
        }
        return null;
    }

    /**
     * 从内容中获取字符串
     */
    private String getStringFromContent(Map<String, Object> content, String key) {
        Object value = content.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 构建详细分析Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildDetailedAnalysis(Map<String, Object> content) {
        Map<String, Object> detailedAnalysis = new HashMap<>();

        String[] keys = {"industryAnalysis", "teamAnalysis", "technologyAnalysis",
                "competitivenessAnalysis", "financialHealth", "investmentValue"};
        String[] outputKeys = {"marketAnalysis", "teamAnalysis", "productAnalysis",
                "businessModelAnalysis", "financialAnalysis", "competitivenessAnalysis"};

        for (int i = 0; i < keys.length; i++) {
            Object value = content.get(keys[i]);
            if (value instanceof Map) {
                Map<String, Object> analysis = (Map<String, Object>) value;
                detailedAnalysis.put(outputKeys[i], Map.of(
                        "score", analysis.getOrDefault("score", 0),
                        "comment", analysis.getOrDefault("text", "")
                ));
            }
        }

        return detailedAnalysis;
    }
}
