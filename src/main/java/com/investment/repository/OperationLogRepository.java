package com.investment.repository;

import com.investment.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    /**
     * 根据用户ID查找操作日志
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 操作日志分页列表
     */
    Page<OperationLog> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据操作类型查找日志
     *
     * @param operationType 操作类型
     * @param pageable      分页参数
     * @return 操作日志分页列表
     */
    Page<OperationLog> findByOperationType(String operationType, Pageable pageable);

    /**
     * 根据模块查找日志
     *
     * @param operationModule   模块
     * @param pageable 分页参数
     * @return 操作日志分页列表
     */
    Page<OperationLog> findByOperationModule(String operationModule, Pageable pageable);

    /**
     * 根据时间范围查找日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页参数
     * @return 操作日志分页列表
     */
    Page<OperationLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据用户ID和时间范围查找日志
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页参数
     * @return 操作日志分页列表
     */
    Page<OperationLog> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 统计用户操作次数
     *
     * @param userId 用户ID
     * @return 操作次数
     */
    long countByUserId(Long userId);

    /**
     * 查询最近的操作日志
     *
     * @param limit 数量限制
     * @return 操作日志列表
     */
    @Query(value = "SELECT * FROM operation_logs ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<OperationLog> findRecentLogs(@Param("limit") int limit);

    /**
     * 统计某模块的操作次数
     *
     * @param operationModule 模块
     * @return 操作次数
     */
    long countByOperationModule(String operationModule);
}
