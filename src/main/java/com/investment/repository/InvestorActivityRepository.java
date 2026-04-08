package com.investment.repository;

import com.investment.entity.InvestorActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 投资人活动数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface InvestorActivityRepository extends JpaRepository<InvestorActivity, Long> {

    /**
     * 根据投资人ID查找活动
     *
     * @param investorUserId 投资人ID
     * @param pageable       分页参数
     * @return 活动分页列表
     */
    Page<InvestorActivity> findByInvestorUserIdOrderByCreatedAtDesc(Long investorUserId, Pageable pageable);

    /**
     * 根据投资人ID和活动类型查找
     *
     * @param investorUserId 投资人ID
     * @param activityType   活动类型
     * @param pageable       分页参数
     * @return 活动分页列表
     */
    Page<InvestorActivity> findByInvestorUserIdAndActivityTypeOrderByCreatedAtDesc(
            Long investorUserId, String activityType, Pageable pageable);

    /**
     * 根据投资人ID和Teaser ID查找活动
     *
     * @param investorUserId 投资人ID
     * @param teaserId       Teaser ID
     * @param activityType   活动类型
     * @return 活动记录
     */
    Optional<InvestorActivity> findByInvestorUserIdAndTeaserIdAndActivityType(
            Long investorUserId, Long teaserId, String activityType);

    /**
     * 检查活动是否存在
     *
     * @param investorUserId 投资人ID
     * @param teaserId       Teaser ID
     * @param activityType   活动类型
     * @return 是否存在
     */
    boolean existsByInvestorUserIdAndTeaserIdAndActivityType(Long investorUserId, Long teaserId, String activityType);

    /**
     * 查找投资人最近的浏览记录
     *
     * @param investorUserId 投资人ID
     * @param pageable       分页参数
     * @return 活动列表
     */
    @Query("SELECT ia FROM InvestorActivity ia WHERE ia.investorUserId = :investorUserId AND ia.activityType = 'VIEW' ORDER BY ia.createdAt DESC")
    List<InvestorActivity> findRecentViews(@Param("investorUserId") Long investorUserId, Pageable pageable);

    /**
     * 统计投资人的浏览次数
     *
     * @param investorUserId 投资人ID
     * @return 浏览次数
     */
    @Query("SELECT COUNT(ia) FROM InvestorActivity ia WHERE ia.investorUserId = :investorUserId AND ia.activityType = 'VIEW'")
    long countByInvestorUserIdAndViewActivity(@Param("investorUserId") Long investorUserId);

    /**
     * 统计投资人的收藏次数
     *
     * @param investorUserId 投资人ID
     * @return 收藏次数
     */
    @Query("SELECT COUNT(ia) FROM InvestorActivity ia WHERE ia.investorUserId = :investorUserId AND ia.activityType = 'FAVORITE'")
    long countByInvestorUserIdAndFavoriteActivity(@Param("investorUserId") Long investorUserId);

    /**
     * 根据投资人ID和Teaser ID删除活动
     *
     * @param investorUserId 投资人ID
     * @param teaserId       Teaser ID
     * @param activityType   活动类型
     */
    @Modifying
    @Query("DELETE FROM InvestorActivity ia WHERE ia.investorUserId = :investorUserId AND ia.teaserId = :teaserId AND ia.activityType = :activityType")
    void deleteByInvestorUserIdAndTeaserIdAndActivityType(
            @Param("investorUserId") Long investorUserId,
            @Param("teaserId") Long teaserId,
            @Param("activityType") String activityType);

    /**
     * 获取投资人指定活动类型的不重复分组名称列表
     *
     * @param investorUserId 投资人ID
     * @param activityType   活动类型
     * @return 分组名称列表
     */
    @Query("SELECT DISTINCT ia.groupName FROM InvestorActivity ia WHERE ia.investorUserId = :investorUserId AND ia.activityType = :activityType AND ia.groupName IS NOT NULL")
    List<String> findDistinctGroupNamesByInvestorUserIdAndActivityType(
            @Param("investorUserId") Long investorUserId,
            @Param("activityType") String activityType);
}
