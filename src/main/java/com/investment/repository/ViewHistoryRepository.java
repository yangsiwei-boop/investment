package com.investment.repository;

import com.investment.entity.ViewHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 浏览记录数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    /**
     * 根据用户ID分页查询浏览记录
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 浏览记录分页列表
     */
    Page<ViewHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 根据用户ID和Teaser ID查找浏览记录
     *
     * @param userId   用户ID
     * @param teaserId Teaser ID
     * @param pageable 分页参数
     * @return 浏览记录列表
     */
    List<ViewHistory> findByUserIdAndTeaserIdOrderByCreatedAtDesc(Long userId, Long teaserId, Pageable pageable);

    /**
     * 统计用户的浏览次数
     *
     * @param userId 用户ID
     * @return 浏览次数
     */
    long countByUserId(Long userId);

    /**
     * 查找用户最近的浏览记录
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 浏览记录列表
     */
    @Query("SELECT vh FROM ViewHistory vh WHERE vh.userId = :userId ORDER BY vh.createdAt DESC")
    List<ViewHistory> findRecentViews(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(vh) FROM ViewHistory vh WHERE vh.createdAt >= :startDate AND vh.createdAt < :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
