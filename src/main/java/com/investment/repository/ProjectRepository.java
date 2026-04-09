package com.investment.repository;

import com.investment.entity.Project;
import com.investment.enums.FinancingStage;
import com.investment.enums.IndustryType;
import com.investment.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 项目数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * 根据项目编码查找项目
     *
     * @param projectCode 项目编码
     * @return 项目
     */
    Optional<Project> findByProjectCode(String projectCode);

    /**
     * 检查项目编码是否存在
     *
     * @param projectCode 项目编码
     * @return 是否存在
     */
    boolean existsByProjectCode(String projectCode);

    /**
     * 根据融资用户ID查找项目列表
     *
     * @param entrepreneurUserId 融资用户ID
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findByEntrepreneurUserId(Long entrepreneurUserId, Pageable pageable);

    /**
     * 根据融资用户ID和状态查找项目列表
     *
     * @param entrepreneurUserId 融资用户ID
     * @param status 项目状态
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findByEntrepreneurUserIdAndStatus(Long entrepreneurUserId, ProjectStatus status, Pageable pageable);

    /**
     * 根据状态查找项目列表
     *
     * @param status 项目状态
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    /**
     * 根据行业查找项目列表
     *
     * @param industry 行业类型
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findByIndustry(IndustryType industry, Pageable pageable);

    /**
     * 根据融资阶段查找项目列表
     *
     * @param financingStage 融资阶段
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findByFinancingStage(String financingStage, Pageable pageable);

    /**
     * 搜索项目（按名称或描述）
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    @Query("SELECT p FROM Project p WHERE p.projectName LIKE %:keyword% OR p.businessDescription LIKE %:keyword%")
    Page<Project> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 统计融资用户的项目数量
     *
     * @param entrepreneurUserId 融资用户ID
     * @return 项目数量
     */
    Long countByEntrepreneurUserId(Long entrepreneurUserId);

    /**
     * 统计各状态的项目数量
     *
     * @param status 项目状态
     * @return 项目数量
     */
    Long countByStatus(ProjectStatus status);

    /**
     * 统计各行业的项目数量
     *
     * @param industry 行业类型
     * @return 项目数量
     */
    Long countByIndustry(IndustryType industry);

    /**
     * 查找热门项目（按浏览次数排序）
     *
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findAllByOrderByViewCountDesc(Pageable pageable);

    /**
     * 查找最新项目（按创建时间排序）
     *
     * @param pageable 分页参数
     * @return 项目分页列表
     */
    Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.createdAt >= :startDate AND p.createdAt < :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.createdAt >= :date")
    Long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    Long countByFinancingStage(FinancingStage financingStage);
}
