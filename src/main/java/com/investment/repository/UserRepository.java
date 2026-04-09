package com.investment.repository;

import com.investment.entity.User;
import com.investment.enums.UserStatus;
import com.investment.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据手机号查找用户
     *
     * @param phone 手机号
     * @return 用户实体
     */
    Optional<User> findByPhone(String phone);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户实体
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据用户类型查找用户列表
     *
     * @param userType 用户类型
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<User> findByUserType(UserType userType, Pageable pageable);

    /**
     * 根据状态查找用户列表
     *
     * @param status 用户状态
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * 根据用户类型和状态查找用户列表
     *
     * @param userType 用户类型
     * @param status 用户状态
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<User> findByUserTypeAndStatus(UserType userType, UserStatus status, Pageable pageable);

    /**
     * 查找已实名认证的用户
     *
     * @param isVerified 是否实名认证
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<User> findByIsVerified(Boolean isVerified, Pageable pageable);

    /**
     * 模糊搜索用户（按手机号或真实姓名）
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    @Query("SELECT u FROM User u WHERE u.phone LIKE %:keyword% OR u.realName LIKE %:keyword%")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 统计指定日期后注册的用户数量
     *
     * @param date 日期
     * @return 用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    Long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt < :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 统计指定时间段内登录的用户数量
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 用户数量
     */
    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE u.lastLoginAt BETWEEN :startDate AND :endDate")
    Long countActiveUsersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 统计各用户类型的数量
     *
     * @param userType 用户类型
     * @return 数量
     */
    Long countByUserType(UserType userType);

    /**
     * 统计各状态的用户数量
     *
     * @param status 用户状态
     * @return 数量
     */
    Long countByStatus(UserStatus status);

    /**
     * 统计指定时间后登录的用户数量
     *
     * @param lastLoginAt 最后登录时间
     * @return 用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginAt >= :lastLoginAt")
    Long countByLastLoginAtAfter(@Param("lastLoginAt") LocalDateTime lastLoginAt);

    /**
     * 多条件筛选用户
     *
     * @param userType 用户类型（可选）
     * @param status 用户状态（可选）
     * @param keyword 关键词（可选）
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    @Query("SELECT u FROM User u WHERE " +
            "(:userType IS NULL OR u.userType = :userType) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:keyword IS NULL OR :keyword = '' OR u.phone LIKE %:keyword% OR u.realName LIKE %:keyword% OR u.email LIKE %:keyword%)")
    Page<User> findWithFilters(@Param("userType") String userType, @Param("status") String status,
                               @Param("keyword") String keyword, Pageable pageable);
}
