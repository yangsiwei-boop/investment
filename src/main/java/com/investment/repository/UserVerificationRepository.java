package com.investment.repository;

import com.investment.entity.UserVerification;
import com.investment.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 实名认证数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {

    /**
     * 根据用户ID查找最新的认证记录
     *
     * @param userId 用户ID
     * @return 认证记录
     */
    Optional<UserVerification> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据用户ID和状态查找认证记录
     *
     * @param userId 用户ID
     * @param status 认证状态
     * @return 认证记录列表
     */
    List<UserVerification> findByUserIdAndVerificationStatus(Long userId, VerificationStatus status);

    /**
     * 根据状态查找认证记录
     *
     * @param status 认证状态
     * @param pageable 分页参数
     * @return 认证记录分页列表
     */
    Page<UserVerification> findByVerificationStatusOrderByCreatedAtDesc(VerificationStatus status, Pageable pageable);

    /**
     * 查找待审核的认证记录
     *
     * @param pageable 分页参数
     * @return 认证记录分页列表
     */
    Page<UserVerification> findByVerificationStatusOrderByCreatedAtAsc(VerificationStatus status, Pageable pageable);

    /**
     * 统计各状态的认证记录数量
     *
     * @param status 认证状态
     * @return 数量
     */
    Long countByVerificationStatus(VerificationStatus status);

    /**
     * 检查用户是否有待审核的认证记录
     *
     * @param userId 用户ID
     * @param status 认证状态
     * @return 是否存在
     */
    boolean existsByUserIdAndVerificationStatus(Long userId, VerificationStatus status);
}
