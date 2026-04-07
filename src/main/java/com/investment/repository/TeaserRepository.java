package com.investment.repository;

import com.investment.entity.Teaser;
import com.investment.enums.TeaserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Teaser数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface TeaserRepository extends JpaRepository<Teaser, Long>, JpaSpecificationExecutor<Teaser> {

    /**
     * 根据项目ID查找Teaser
     *
     * @param projectId 项目ID
     * @return Teaser
     */
    Optional<Teaser> findByProjectId(Long projectId);

    /**
     * 根据状态查找Teaser列表
     *
     * @param status   Teaser状态
     * @param pageable 分页参数
     * @return Teaser分页列表
     */
    Page<Teaser> findByStatus(TeaserStatus status, Pageable pageable);

    /**
     * 根据项目ID和状态查找Teaser
     *
     * @param projectId 项目ID
     * @param status    Teaser状态
     * @return Teaser
     */
    Optional<Teaser> findByProjectIdAndStatus(Long projectId, TeaserStatus status);

    /**
     * 查找热门Teaser（按浏览次数排序）
     *
     * @param status   Teaser状态
     * @param pageable 分页参数
     * @return Teaser分页列表
     */
    Page<Teaser> findAllByStatusOrderByViewCountDesc(TeaserStatus status, Pageable pageable);

    /**
     * 统计项目的Teaser数量
     *
     * @param projectId 项目ID
     * @return Teaser数量
     */
    Long countByProjectId(Long projectId);

    /**
     * 统计各状态的Teaser数量
     *
     * @param status Teaser状态
     * @return Teaser数量
     */
    Long countByStatus(TeaserStatus status);

    /**
     * 搜索Teaser
     *
     * @param keyword  关键词
     * @param status   状态
     * @param pageable 分页参数
     * @return Teaser分页列表
     */
    @Query("SELECT t FROM Teaser t WHERE t.status = :status AND (t.title LIKE %:keyword% OR t.aiSummary LIKE %:keyword%)")
    Page<Teaser> searchByKeyword(@Param("keyword") String keyword, @Param("status") TeaserStatus status, Pageable pageable);

    /**
     * 查找AI分析已就绪的Teaser
     *
     * @param pageable 分页参数
     * @return Teaser分页列表
     */
    Page<Teaser> findByAiAnalysisReadyTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 检查项目是否存在Teaser
     *
     * @param projectId 项目ID
     * @return 是否存在
     */
    boolean existsByProjectId(Long projectId);

    /**
     * 检查项目是否存在指定状态的Teaser
     *
     * @param projectId 项目ID
     * @param status    Teaser状态
     * @return 是否存在
     */
    boolean existsByProjectIdAndStatus(Long projectId, TeaserStatus status);

    /**
     * 根据融资用户ID查找Teaser列表
     *
     * @param entrepreneurUserId 融资用户ID
     * @param pageable           分页参数
     * @return Teaser分页列表
     */
    @Query("SELECT t FROM Teaser t WHERE t.project.entrepreneurUser.id = :entrepreneurUserId ORDER BY t.createdAt DESC")
    Page<Teaser> findByEntrepreneurUserId(@Param("entrepreneurUserId") Long entrepreneurUserId, Pageable pageable);

    /**
     * 根据融资用户ID和状态统计Teaser数量
     *
     * @param entrepreneurUserId 融资用户ID
     * @param status             Teaser状态
     * @return 数量
     */
    @Query("SELECT COUNT(t) FROM Teaser t WHERE t.project.entrepreneurUser.id = :entrepreneurUserId AND t.status = :status")
    Long countByEntrepreneurUserIdAndStatus(@Param("entrepreneurUserId") Long entrepreneurUserId, @Param("status") TeaserStatus status);

    /**
     * 统计融资用户所有Teaser的浏览次数
     *
     * @param entrepreneurUserId 融资用户ID
     * @return 总浏览次数
     */
    @Query("SELECT COALESCE(SUM(t.viewCount), 0) FROM Teaser t WHERE t.project.entrepreneurUser.id = :entrepreneurUserId")
    Long sumViewCountByEntrepreneurUserId(@Param("entrepreneurUserId") Long entrepreneurUserId);

    /**
     * 统计融资用户所有Teaser的收藏次数
     *
     * @param entrepreneurUserId 融资用户ID
     * @return 总收藏次数
     */
    @Query("SELECT COALESCE(SUM(t.favoriteCount), 0) FROM Teaser t WHERE t.project.entrepreneurUser.id = :entrepreneurUserId")
    Long sumFavoriteCountByEntrepreneurUserId(@Param("entrepreneurUserId") Long entrepreneurUserId);

    /**
     * 统计所有Teaser的总浏览次数
     *
     * @return 总浏览次数
     */
    @Query("SELECT COALESCE(SUM(t.viewCount), 0) FROM Teaser t")
    Long sumAllViewCount();

    /**
     * 统计所有Teaser的总收藏次数
     *
     * @return 总收藏次数
     */
    @Query("SELECT COALESCE(SUM(t.favoriteCount), 0) FROM Teaser t")
    Long sumAllFavoriteCount();
}
