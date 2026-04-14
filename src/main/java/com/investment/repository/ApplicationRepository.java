package com.investment.repository;

import com.investment.entity.Application;
import com.investment.enums.ApplicationStatus;
import com.investment.enums.ApplicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 申请数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /**
     * 根据投资人ID查找申请列表
     *
     * @param investorUserId 投资人ID
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.investorUser.id = :investorUserId ORDER BY a.createdAt DESC")
    Page<Application> findByInvestorUserId(@Param("investorUserId") Long investorUserId, Pageable pageable);

    /**
     * 根据融资用户ID查找申请列表
     *
     * @param entrepreneurUserId 融资用户ID
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.entrepreneurUser.id = :entrepreneurUserId ORDER BY a.createdAt DESC")
    Page<Application> findByEntrepreneurUserId(@Param("entrepreneurUserId") Long entrepreneurUserId, Pageable pageable);

    /**
     * 根据项目ID查找申请列表
     *
     * @param projectId 项目ID
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.project.id = :projectId ORDER BY a.createdAt DESC")
    Page<Application> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    /**
     * 根据申请类型和状态查找
     *
     * @param applicationType 申请类型
     * @param status 状态
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    Page<Application> findByApplicationTypeAndApplicationStatus(ApplicationType applicationType, ApplicationStatus status, Pageable pageable);

    /**
     * 查找待审核的申请
     *
     * @param entrepreneurUserId 融资用户ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.entrepreneurUser.id = :entrepreneurUserId AND a.applicationStatus = :status ORDER BY a.createdAt DESC")
    Page<Application> findByEntrepreneurUserIdAndStatus(@Param("entrepreneurUserId") Long entrepreneurUserId, @Param("status") ApplicationStatus status, Pageable pageable);

    /**
     * 统计各状态的申请数量
     *
     * @param status 状态
     * @return 数量
     */
    Long countByApplicationStatus(ApplicationStatus status);

    /**
     * 统计投资人的申请数量
     *
     * @param investorUserId 投资人ID
     * @return 数量
     */
    @Query("SELECT COUNT(a) FROM Application a WHERE a.investorUser.id = :investorUserId")
    Long countByInvestorUserId(@Param("investorUserId") Long investorUserId);

    /**
     * 检查是否已存在相同的申请
     *
     * @param investorUserId 投资人ID
     * @param projectId 项目ID
     * @param applicationType 申请类型
     * @param status 状态
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Application a WHERE a.investorUser.id = :investorUserId AND a.project.id = :projectId AND a.applicationType = :applicationType AND a.applicationStatus = :status")
    boolean existsByInvestorUserIdAndProjectIdAndApplicationTypeAndStatus(
            @Param("investorUserId") Long investorUserId,
            @Param("projectId") Long projectId,
            @Param("applicationType") ApplicationType applicationType,
            @Param("status") ApplicationStatus status);

    /**
     * 查找用户已批准的申请
     *
     * @param investorUserId 投资人ID
     * @param projectId 项目ID
     * @param status 状态
     * @return 申请列表
     */
    @Query("SELECT a FROM Application a WHERE a.investorUser.id = :investorUserId AND a.project.id = :projectId AND a.applicationStatus = :status")
    List<Application> findByInvestorUserIdAndProjectIdAndStatus(
            @Param("investorUserId") Long investorUserId,
            @Param("projectId") Long projectId,
            @Param("status") ApplicationStatus status);

    /**
     * 统计融资用户待处理的申请数量
     *
     * @param entrepreneurUserId 融资用户ID
     * @return 数量
     */
    @Query("SELECT COUNT(a) FROM Application a WHERE a.entrepreneurUser.id = :entrepreneurUserId AND a.applicationStatus = 'PENDING'")
    Long countPendingByUserId(@Param("entrepreneurUserId") Long entrepreneurUserId);

    /**
     * 检查是否已存在相同的申请（使用申请人ID和Teaser ID）
     *
     * @param applicantId 申请人ID
     * @param teaserId Teaser ID
     * @param applicationType 申请类型
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Application a WHERE a.investorUser.id = :applicantId AND a.teaserId = :teaserId AND a.applicationType = :applicationType")
    boolean existsByApplicantIdAndTeaserIdAndApplicationType(
            @Param("applicantId") Long applicantId,
            @Param("teaserId") Long teaserId,
            @Param("applicationType") ApplicationType applicationType);

    /**
     * 根据申请人ID和状态查找申请
     *
     * @param applicantId 申请人ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.investorUser.id = :applicantId AND a.applicationStatus = :status ORDER BY a.createdAt DESC")
    Page<Application> findByApplicantIdAndStatus(@Param("applicantId") Long applicantId, @Param("status") ApplicationStatus status, Pageable pageable);

    /**
     * 根据申请人ID查找申请
     *
     * @param applicantId 申请人ID
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.investorUser.id = :applicantId ORDER BY a.createdAt DESC")
    Page<Application> findByApplicantId(@Param("applicantId") Long applicantId, Pageable pageable);

    /**
     * 根据融资用户ID查找申请（简化方法名）
     *
     * @param entrepreneurId 融资用户ID
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.entrepreneurUser.id = :entrepreneurId ORDER BY a.createdAt DESC")
    Page<Application> findByEntrepreneurId(@Param("entrepreneurId") Long entrepreneurId, Pageable pageable);

    /**
     * 根据融资用户ID和状态查找申请
     *
     * @param entrepreneurId 融资用户ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    @Query("SELECT a FROM Application a WHERE a.entrepreneurUser.id = :entrepreneurId AND a.applicationStatus = :status ORDER BY a.createdAt DESC")
    Page<Application> findByEntrepreneurIdAndStatus(@Param("entrepreneurId") Long entrepreneurId, @Param("status") ApplicationStatus status, Pageable pageable);

    /**
     * 根据状态查找所有申请（管理端）
     *
     * @param status 状态
     * @param pageable 分页参数
     * @return 申请分页列表
     */
    Page<Application> findByApplicationStatusOrderByCreatedAtDesc(ApplicationStatus status, Pageable pageable);
}
