package com.investment.repository;

import com.investment.entity.InvestorQuestion;
import com.investment.enums.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投资人问题库数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface InvestorQuestionRepository extends JpaRepository<InvestorQuestion, Long> {

    /**
     * 查找所有模板问题（按排序号升序）
     *
     * @return 问题列表
     */
    List<InvestorQuestion> findByIsTemplateTrueOrderBySortOrderAsc();

    /**
     * 根据分类查找模板问题
     *
     * @param category 问题分类
     * @return 问题列表
     */
    List<InvestorQuestion> findByCategoryAndIsTemplateTrueOrderBySortOrderAsc(QuestionCategory category);

    /**
     * 查找常用问题（按使用次数降序）
     *
     * @param pageable 分页参数
     * @return 问题分页列表
     */
    Page<InvestorQuestion> findByIsFrequentTrueOrderByUsageCountDesc(Pageable pageable);

    /**
     * 查找模板问题（按排序号升序）
     *
     * @param pageable 分页参数
     * @return 问题分页列表
     */
    Page<InvestorQuestion> findByIsTemplateTrueOrderBySortOrderAsc(Pageable pageable);

    /**
     * 搜索问题
     *
     * @param keyword  关键词
     * @param pageable 分页参数
     * @return 问题分页列表
     */
    @Query("SELECT iq FROM InvestorQuestion iq WHERE iq.questionTitle LIKE %:keyword% OR iq.questionDescription LIKE %:keyword%")
    Page<InvestorQuestion> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据投资人ID查找问题
     *
     * @param investorUserId 投资人ID
     * @param pageable       分页参数
     * @return 问题分页列表
     */
    @Query("SELECT iq FROM InvestorQuestion iq WHERE iq.investorUser.id = :investorUserId ORDER BY iq.sortOrder ASC")
    Page<InvestorQuestion> findByInvestorUserId(@Param("investorUserId") Long investorUserId, Pageable pageable);

    /**
     * 根据投资人ID和分类查找问题
     *
     * @param investorUserId 投资人ID
     * @param category       问题分类
     * @param pageable       分页参数
     * @return 问题分页列表
     */
    @Query("SELECT iq FROM InvestorQuestion iq WHERE iq.investorUser.id = :investorUserId AND iq.category = :category ORDER BY iq.sortOrder ASC")
    Page<InvestorQuestion> findByInvestorUserIdAndCategory(
            @Param("investorUserId") Long investorUserId,
            @Param("category") QuestionCategory category,
            Pageable pageable);

    /**
     * 查找使用次数最多的问题
     *
     * @param pageable 分页参数
     * @return 问题列表
     */
    @Query("SELECT iq FROM InvestorQuestion iq WHERE iq.isTemplate = true ORDER BY iq.usageCount DESC")
    List<InvestorQuestion> findTopUsedTemplateQuestions(Pageable pageable);

    /**
     * 统计投资人的问题数量
     *
     * @param investorUserId 投资人ID
     * @return 数量
     */
    @Query("SELECT COUNT(iq) FROM InvestorQuestion iq WHERE iq.investorUser.id = :investorUserId")
    Long countByInvestorUserId(@Param("investorUserId") Long investorUserId);
}
