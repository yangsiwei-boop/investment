package com.investment.repository;

import com.investment.entity.InvestorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 投资人资料数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface InvestorProfileRepository extends JpaRepository<InvestorProfile, Long> {

    /**
     * 根据用户ID查找投资人资料
     *
     * @param userId 用户ID
     * @return 投资人资料
     */
    Optional<InvestorProfile> findByUserId(Long userId);

    /**
     * 检查用户ID是否存在
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean existsByUserId(Long userId);

    /**
     * 根据机构名称模糊查询
     *
     * @param institutionName 机构名称
     * @param pageable 分页参数
     * @return 投资人资料分页列表
     */
    Page<InvestorProfile> findByInstitutionNameContaining(String institutionName, Pageable pageable);

    /**
     * 查找已认证的投资人
     *
     * @param isVerified 是否认证
     * @param pageable 分页参数
     * @return 投资人资料分页列表
     */
    Page<InvestorProfile> findByIsVerified(Boolean isVerified, Pageable pageable);

    /**
     * 根据认证等级查找
     *
     * @param verificationLevel 认证等级
     * @param pageable 分页参数
     * @return 投资人资料分页列表
     */
    Page<InvestorProfile> findByVerificationLevel(String verificationLevel, Pageable pageable);

    /**
     * 查找投资阶段偏好匹配的投资人
     *
     * @param stage 投资阶段
     * @return 投资人列表
     */
    @Query("SELECT ip FROM InvestorProfile ip WHERE ip.investmentStage LIKE %:stage%")
    List<InvestorProfile> findByInvestmentStageContaining(@Param("stage") String stage);

    /**
     * 查找投资行业偏好匹配的投资人
     *
     * @param industry 行业
     * @return 投资人列表
     */
    @Query("SELECT ip FROM InvestorProfile ip WHERE ip.investmentIndustries LIKE %:industry%")
    List<InvestorProfile> findByInvestmentIndustriesContaining(@Param("industry") String industry);

    /**
     * 统计已认证投资人数量
     *
     * @return 数量
     */
    @Query("SELECT COUNT(ip) FROM InvestorProfile ip WHERE ip.isVerified = true")
    Long countVerifiedInvestors();
}
