package com.investment.repository;

import com.investment.entity.EntrepreneurProfile;
import com.investment.enums.IndustryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 融资用户资料数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface EntrepreneurProfileRepository extends JpaRepository<EntrepreneurProfile, Long> {

    /**
     * 根据用户ID查找融资用户资料
     *
     * @param userId 用户ID
     * @return 融资用户资料
     */
    Optional<EntrepreneurProfile> findByUserId(Long userId);

    /**
     * 检查用户ID是否存在
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean existsByUserId(Long userId);

    /**
     * 根据公司名称模糊查询
     *
     * @param companyName 公司名称
     * @param pageable 分页参数
     * @return 融资用户资料分页列表
     */
    Page<EntrepreneurProfile> findByCompanyNameContaining(String companyName, Pageable pageable);

    /**
     * 根据行业查找
     *
     * @param industry 行业类型
     * @param pageable 分页参数
     * @return 融资用户资料分页列表
     */
    Page<EntrepreneurProfile> findByIndustry(IndustryType industry, Pageable pageable);

    /**
     * 根据所在地查找
     *
     * @param location 所在地
     * @param pageable 分页参数
     * @return 融资用户资料分页列表
     */
    Page<EntrepreneurProfile> findByLocationContaining(String location, Pageable pageable);

    /**
     * 统计各行业的企业数量
     *
     * @param industry 行业
     * @return 数量
     */
    Long countByIndustry(IndustryType industry);

    /**
     * 查找资料完善度低于指定值的记录
     *
     * @param threshold 阈值
     * @return 融资用户资料列表
     */
    List<EntrepreneurProfile> findByProfileCompletionRateLessThan(Integer threshold);
}
