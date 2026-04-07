package com.investment.service;

import com.investment.entity.OperationLog;
import com.investment.entity.User;
import com.investment.repository.OperationLogRepository;
import com.investment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 操作日志服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;
    private final UserRepository userRepository;

    /**
     * 记录操作日志
     *
     * @param userId        用户ID
     * @param operationType 操作类型
     * @param module        模块
     * @param description   描述
     * @param requestParams 请求参数
     * @param result        结果
     * @param ip            IP地址
     */
    @Async
    @Transactional
    public void log(Long userId, String operationType, String module, String description,
                    String requestParams, String result, String ip) {
        User user = userId != null ? userRepository.getReferenceById(userId) : null;

        OperationLog operationLog = OperationLog.builder()
                .user(user)
                .operationType(operationType)
                .operationModule(module)
                .operationDescription(description)
                .requestParams(requestParams)
                .ipAddress(ip)
                .operationTime(LocalDateTime.now())
                .build();

        operationLogRepository.save(operationLog);
    }

    /**
     * 记录操作日志（简化版）
     *
     * @param userId        用户ID
     * @param operationType 操作类型
     * @param module        模块
     * @param description   描述
     */
    @Async
    public void log(Long userId, String operationType, String module, String description) {
        log(userId, operationType, module, description, null, null, null);
    }

    /**
     * 查询用户操作日志
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页数量
     * @return 日志分页
     */
    public Page<OperationLog> getUserLogs(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return operationLogRepository.findByUserId(userId, pageable);
    }

    /**
     * 查询模块操作日志
     *
     * @param module 模块
     * @param page   页码
     * @param size   每页数量
     * @return 日志分页
     */
    public Page<OperationLog> getModuleLogs(String module, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return operationLogRepository.findByOperationModule(module, pageable);
    }

    /**
     * 查询时间范围内的操作日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     * @param size      每页数量
     * @return 日志分页
     */
    public Page<OperationLog> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return operationLogRepository.findByCreatedAtBetween(startTime, endTime, pageable);
    }

    /**
     * 清理过期的操作日志
     *
     * @param daysToKeep 保留天数
     */
    @Transactional
    public void cleanOldLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        log.info("Cleaning operation logs before: {}", cutoffDate);

        // 这里可以添加删除逻辑
        // operationLogRepository.deleteByCreatedAtBefore(cutoffDate);
    }
}
