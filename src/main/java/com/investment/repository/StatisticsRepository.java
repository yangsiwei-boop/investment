package com.investment.repository;

import com.investment.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 统计数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    /**
     * 根据统计日期和类型查找统计
     *
     * @param statDate 统计日期
     * @param statType 统计类型
     * @return 统计数据
     */
    Optional<Statistics> findByStatDateAndStatType(LocalDate statDate, String statType);

    /**
     * 根据统计类型和日期范围查找统计列表
     *
     * @param statType  统计类型
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计列表
     */
    @Query("SELECT s FROM Statistics s WHERE s.statType = :statType " +
            "AND s.statDate >= :startDate AND s.statDate <= :endDate " +
            "ORDER BY s.statDate ASC")
    List<Statistics> findByStatTypeAndDateRange(
            @Param("statType") String statType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * 获取某类型的最新统计数据
     *
     * @param statType 统计类型
     * @return 最新统计
     */
    Optional<Statistics> findFirstByStatTypeOrderByStatDateDesc(String statType);

    /**
     * 获取最近N天的统计数据
     *
     * @param statType 统计类型
     * @param days     天数
     * @return 统计列表
     */
    @Query(value = "SELECT * FROM statistics WHERE stat_type = :statType " +
            "ORDER BY stat_date DESC LIMIT :days", nativeQuery = true)
    List<Statistics> findRecentStats(@Param("statType") String statType, @Param("days") int days);

    /**
     * 统计某类型的总记录数
     *
     * @param statType 统计类型
     * @return 记录数
     */
    long countByStatType(String statType);

    /**
     * 根据统计日期、类型和指标键查找统计
     *
     * @param statDate   统计日期
     * @param statType   统计类型
     * @param metricKey  指标键
     * @return 统计数据
     */
    Optional<Statistics> findByStatDateAndStatTypeAndMetricKey(LocalDate statDate, String statType, String metricKey);
}
