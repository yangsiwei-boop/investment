package com.investment.repository;

import com.investment.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 收藏数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * 根据用户ID和Teaser ID查找收藏
     *
     * @param userId   用户ID
     * @param teaserId Teaser ID
     * @return 收藏记录
     */
    Optional<Favorite> findByUserIdAndTeaserId(Long userId, Long teaserId);

    /**
     * 检查是否已收藏
     *
     * @param userId   用户ID
     * @param teaserId Teaser ID
     * @return 是否已收藏
     */
    boolean existsByUserIdAndTeaserId(Long userId, Long teaserId);

    /**
     * 根据用户ID分页查询收藏列表（按创建时间降序）
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 收藏分页列表
     */
    Page<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 统计用户的收藏数量
     *
     * @param userId 用户ID
     * @return 收藏数量
     */
    long countByUserId(Long userId);

    /**
     * 删除收藏
     *
     * @param userId   用户ID
     * @param teaserId Teaser ID
     */
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.userId = :userId AND f.teaserId = :teaserId")
    void deleteByUserIdAndTeaserId(@Param("userId") Long userId, @Param("teaserId") Long teaserId);

    /**
     * 获取用户的所有分组名称
     */
    @Query("SELECT DISTINCT f.groupName FROM Favorite f WHERE f.userId = :userId AND f.groupName IS NOT NULL")
    List<String> findDistinctGroupNamesByUserId(@Param("userId") Long userId);
}
