package com.investment.service;

import com.investment.entity.Statistics;
import com.investment.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    /**
     * 记录每日统计
     *
     * @param statType    统计类型
     * @param statDate    统计日期
     * @param metricKey   指标键
     * @param metricValue 指标值
     * @param dimensions  维度信息(JSON格式)
     */
    @Transactional
    public void recordDailyStats(String statType, LocalDate statDate, String metricKey,
                                  BigDecimal metricValue, String dimensions) {
        Statistics stats = Statistics.builder()
                .statType(statType)
                .statDate(statDate)
                .metricKey(metricKey)
                .metricValue(metricValue)
                .dimensions(dimensions)
                .build();
        statisticsRepository.save(stats);
    }

    /**
     * 获取指定日期范围的统计数据
     *
     * @param statType  统计类型
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计列表
     */
    public List<Statistics> getStatsByDateRange(String statType, LocalDate startDate, LocalDate endDate) {
        return statisticsRepository.findByStatTypeAndDateRange(statType, startDate, endDate);
    }

    /**
     * 获取最近N天的统计数据
     *
     * @param statType 统计类型
     * @param days     天数
     * @return 统计列表
     */
    public List<Statistics> getRecentStats(String statType, int days) {
        return statisticsRepository.findRecentStats(statType, days);
    }

    /**
     * 增加统计计数
     *
     * @param statType  统计类型
     * @param metricKey 指标键
     */
    @Transactional
    public void incrementCount(String statType, String metricKey) {
        LocalDate today = LocalDate.now();
        Statistics stats = statisticsRepository.findByStatDateAndStatTypeAndMetricKey(today, statType, metricKey)
                .orElse(Statistics.builder()
                        .statType(statType)
                        .statDate(today)
                        .metricKey(metricKey)
                        .metricValue(BigDecimal.ZERO)
                        .build());

        BigDecimal currentValue = stats.getMetricValue() != null ? stats.getMetricValue() : BigDecimal.ZERO;
        stats.setMetricValue(currentValue.add(BigDecimal.ONE));

        statisticsRepository.save(stats);
    }

    /**
     * 获取统计汇总
     *
     * @param statType 统计类型
     * @param days     天数
     * @return 汇总数据
     */
    public BigDecimal getStatsSummary(String statType, String metricKey, int days) {
        List<Statistics> stats = getRecentStats(statType, days);

        return stats.stream()
                .filter(s -> metricKey.equals(s.getMetricKey()))
                .map(Statistics::getMetricValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * @deprecated 此方法不再支持，请使用 {@link #recordDailyStats(String, LocalDate, String, BigDecimal, String)}
     */
    @Deprecated
    public void recordDailyStats(String statType, LocalDate statDate, Map<String, Object> data) {
        throw new UnsupportedOperationException(
                "This method is no longer supported. Please use recordDailyStats(String statType, LocalDate statDate, " +
                "String metricKey, BigDecimal metricValue, String dimensions) instead.");
    }

    /**
     * @deprecated 此方法不再支持，请使用 {@link #getStatsSummary(String, String, int)}
     */
    @Deprecated
    public Map<String, Long> getStatsSummary(String statType, int days) {
        throw new UnsupportedOperationException(
                "This method is no longer supported. Please use getStatsSummary(String statType, String metricKey, int days) instead.");
    }
}
