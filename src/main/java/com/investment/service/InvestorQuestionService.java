package com.investment.service;

import com.investment.entity.InvestorQuestion;
import com.investment.entity.User;
import com.investment.enums.QuestionCategory;
import com.investment.repository.InvestorQuestionRepository;
import com.investment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 投资人问题库服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestorQuestionService {

    private final InvestorQuestionRepository questionRepository;
    private final UserRepository userRepository;

    /**
     * 获取所有问题模板
     *
     * @return 问题列表
     */
    public List<InvestorQuestion> getAllQuestions() {
        return questionRepository.findByIsTemplateTrueOrderBySortOrderAsc();
    }

    /**
     * 获取指定分类的问题
     *
     * @param category 分类
     * @return 问题列表
     */
    public List<InvestorQuestion> getQuestionsByCategory(QuestionCategory category) {
        return questionRepository.findByCategoryAndIsTemplateTrueOrderBySortOrderAsc(category);
    }

    /**
     * 获取热门问题
     *
     * @param limit 数量限制
     * @return 问题列表
     */
    public List<InvestorQuestion> getHotQuestions(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return questionRepository.findByIsFrequentTrueOrderByUsageCountDesc(pageable).getContent();
    }

    /**
     * 增加问题使用次数
     *
     * @param questionId 问题ID
     */
    @Transactional
    public void incrementUsageCount(Long questionId) {
        questionRepository.findById(questionId).ifPresent(question -> {
            question.setUsageCount(question.getUsageCount() + 1);
            question.setLastUsedAt(java.time.LocalDateTime.now());
            questionRepository.save(question);
        });
    }

    /**
     * 创建问题
     *
     * @param investorUserId 投资人ID
     * @param questionTitle  问题标题
     * @param category       分类
     * @param description    描述
     * @return 问题
     */
    @Transactional
    public InvestorQuestion createQuestion(Long investorUserId, String questionTitle, QuestionCategory category, String description) {
        User investorUser = userRepository.findById(investorUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InvestorQuestion investorQuestion = InvestorQuestion.builder()
                .investorUser(investorUser)
                .questionTitle(questionTitle)
                .category(category)
                .questionDescription(description)
                .isFrequent(false)
                .isTemplate(false)
                .usageCount(0)
                .sortOrder(0)
                .build();

        return questionRepository.save(investorQuestion);
    }

    /**
     * 更新问题
     *
     * @param questionId    问题ID
     * @param questionTitle 问题标题
     * @param category      分类
     * @param description   描述
     * @return 问题
     */
    @Transactional
    public InvestorQuestion updateQuestion(Long questionId, String questionTitle, QuestionCategory category, String description) {
        InvestorQuestion investorQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        investorQuestion.setQuestionTitle(questionTitle);
        investorQuestion.setCategory(category);
        investorQuestion.setQuestionDescription(description);

        return questionRepository.save(investorQuestion);
    }

    /**
     * 删除问题
     *
     * @param questionId 问题ID
     */
    @Transactional
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }
}
