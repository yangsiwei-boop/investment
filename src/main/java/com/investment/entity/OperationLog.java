package com.investment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 操作日志实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operation_logs")
public class OperationLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作用户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 操作类型
     */
    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType;

    /**
     * 操作模块
     */
    @Column(name = "operation_module", length = 50)
    private String operationModule;

    /**
     * 操作描述
     */
    @Column(name = "operation_description", columnDefinition = "TEXT")
    private String operationDescription;

    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * 用户代理
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    /**
     * 请求方法
     */
    @Column(name = "request_method", length = 10)
    private String requestMethod;

    /**
     * 请求URL
     */
    @Column(name = "request_url", length = 500)
    private String requestUrl;

    /**
     * 请求参数
     */
    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    /**
     * 响应状态码
     */
    @Column(name = "response_status")
    private Integer responseStatus;

    /**
     * 响应时间（毫秒）
     */
    @Column(name = "response_time")
    private Long responseTime;

    /**
     * 操作时间
     */
    @Column(name = "operation_time")
    private LocalDateTime operationTime;
}
