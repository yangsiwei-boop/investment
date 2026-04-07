package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据统计实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistics")
public class Statistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 统计日期
     */
    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    /**
     * 统计类型
     */
    @Column(name = "stat_type", nullable = false, length = 20)
    private String statType;

    /**
     * 指标key
     */
    @Column(name = "metric_key", nullable = false, length = 50)
    private String metricKey;

    /**
     * 指标值
     */
    @Column(name = "metric_value", nullable = false, precision = 20, scale = 2)
    private BigDecimal metricValue;

    /**
     * 维度信息(JSON格式)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dimensions", columnDefinition = "json")
    private String dimensions;
}
