package com.investment.repository;

import com.investment.entity.InvestmentAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 投资分析数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface InvestmentAnalysisRepository extends JpaRepository<InvestmentAnalysis, Long> {

    /**
     * 根据Teaser ID查找分析记录
     *
     * @param teaserId Teaser ID
     * @return 分析记录
     */
    List<InvestmentAnalysis> findByTeaserIdOrderByCreatedAtDesc(Long teaserId);

    /**
     * 根据投资人ID查找分析记录
     *
     * @param investorUserId 投资人ID
     * @param pageable 分页参数
     * @return 分析记录分页列表
     */
    Page<InvestmentAnalysis> findByInvestorUserIdOrderByCreatedAtDesc(Long investorUserId, Pageable pageable);

    /**
     * 根据Teaser ID和投资人ID查找分析记录
     *
     * @param teaserId Teaser ID
     * @param investorUserId 投资人ID
     * @return 分析记录
     */
    Optional<InvestmentAnalysis> findByTeaserIdAndInvestorUserId(Long teaserId, Long investorUserId);

    /**
     * 查找指定项目的所有分析记录
     *
     * @param projectId 项目ID
     * @param pageable 分页参数
     * @return 分析记录分页列表
     */
    @Query("SELECT ia FROM InvestmentAnalysis ia WHERE ia.teaser.project.id = :projectId ORDER BY ia.createdAt DESC")
    Page<InvestmentAnalysis> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    /**
     * 统计投资人的分析数量
     *
     * @param investorUserId 投资人ID
     * @return 数量
     */
    Long countByInvestorUserId(Long investorUserId);

    /**
     * 统计Teaser的分析数量
     *
     * @param teaserId Teaser ID
     * @return 数量
     */
    Long countByTeaserId(Long teaserId);

    /**
     * 查找综合评分最高的分析
     *
     * @param teaserId Teaser ID
     * @return 分析记录
     */
    Optional<InvestmentAnalysis> findFirstByTeaserIdOrderByOverallScoreDesc(Long teaserId);

    /**
     * 检查是否存在指定Teaser和投资人的分析
     *
     * @param teaserId Teaser ID
     * @param investorUserId 投资人ID
     * @return 是否存在
     */
    boolean existsByTeaserIdAndInvestorUserId(Long teaserId, Long investorUserId);
}
