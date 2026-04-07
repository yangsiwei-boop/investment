package com.investment.repository;

import com.investment.entity.BusinessPlan;
import com.investment.enums.UploadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商业计划书数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface BusinessPlanRepository extends JpaRepository<BusinessPlan, Long> {

    /**
     * 根据项目ID查找BP列表（按创建时间降序）
     *
     * @param projectId 项目ID
     * @return BP列表
     */
    @Query("SELECT bp FROM BusinessPlan bp WHERE bp.project.id = :projectId ORDER BY bp.createdAt DESC")
    List<BusinessPlan> findByProjectIdOrderByCreatedAtDesc(@Param("projectId") Long projectId);

    /**
     * 根据项目ID查找BP（按创建时间降序）
     *
     * @param projectId 项目ID
     * @param pageable  分页参数
     * @return BP分页列表
     */
    @Query("SELECT bp FROM BusinessPlan bp WHERE bp.project.id = :projectId ORDER BY bp.createdAt DESC")
    Page<BusinessPlan> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    /**
     * 根据项目ID查找最新BP
     *
     * @param projectId 项目ID
     * @return 最新BP
     */
    @Query("SELECT bp FROM BusinessPlan bp WHERE bp.project.id = :projectId ORDER BY bp.createdAt DESC LIMIT 1")
    Optional<BusinessPlan> findFirstByProjectIdOrderByCreatedAtDesc(@Param("projectId") Long projectId);

    /**
     * 根据项目ID和上传状态查找BP
     *
     * @param projectId   项目ID
     * @param uploadStatus 上传状态
     * @return BP列表
     */
    @Query("SELECT bp FROM BusinessPlan bp WHERE bp.project.id = :projectId AND bp.uploadStatus = :uploadStatus ORDER BY bp.createdAt DESC")
    List<BusinessPlan> findByProjectIdAndUploadStatus(
            @Param("projectId") Long projectId,
            @Param("uploadStatus") UploadStatus uploadStatus);

    /**
     * 根据融资用户ID查找BP
     *
     * @param entrepreneurUserId 融资用户ID
     * @param pageable           分页参数
     * @return BP分页列表
     */
    @Query("SELECT bp FROM BusinessPlan bp WHERE bp.project.entrepreneurUser.id = :entrepreneurUserId ORDER BY bp.createdAt DESC")
    Page<BusinessPlan> findByEntrepreneurUserId(@Param("entrepreneurUserId") Long entrepreneurUserId, Pageable pageable);

    /**
     * 统计项目的BP数量
     *
     * @param projectId 项目ID
     * @return 数量
     */
    @Query("SELECT COUNT(bp) FROM BusinessPlan bp WHERE bp.project.id = :projectId")
    long countByProjectId(@Param("projectId") Long projectId);

    /**
     * 查找待处理的BP
     *
     * @param pageable 分页参数
     * @return BP分页列表
     */
    @Query("SELECT bp FROM BusinessPlan bp WHERE bp.uploadStatus = 'UPLOADING' ORDER BY bp.createdAt ASC")
    Page<BusinessPlan> findPendingBps(Pageable pageable);

    /**
     * 检查项目是否有已完成的BP
     *
     * @param projectId 项目ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(bp) > 0 THEN true ELSE false END FROM BusinessPlan bp " +
            "WHERE bp.project.id = :projectId AND bp.uploadStatus = 'COMPLETED'")
    boolean hasCompletedBp(@Param("projectId") Long projectId);

    /**
     * 检查项目是否存在BP
     *
     * @param projectId 项目ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(bp) > 0 THEN true ELSE false END FROM BusinessPlan bp WHERE bp.project.id = :projectId")
    boolean existsByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据文件哈希查找BP（用于去重）
     *
     * @param fileHash 文件哈希
     * @return BP
     */
    Optional<BusinessPlan> findByFileHash(String fileHash);
}
