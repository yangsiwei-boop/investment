package com.investment.repository;

import com.investment.entity.QaRecord;
import com.investment.enums.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问答记录数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface QaRecordRepository extends JpaRepository<QaRecord, Long> {

    /**
     * 根据投资人ID查找问答记录
     *
     * @param investorUserId 投资人ID
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.investorUser.id = :investorUserId ORDER BY q.sentAt DESC")
    Page<QaRecord> findByInvestorUserIdOrderBySentAtDesc(@Param("investorUserId") Long investorUserId, Pageable pageable);

    /**
     * 根据融资用户ID查找问答记录
     *
     * @param entrepreneurUserId 融资用户ID
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.entrepreneurUser.id = :entrepreneurUserId ORDER BY q.sentAt DESC")
    Page<QaRecord> findByEntrepreneurUserIdOrderBySentAtDesc(@Param("entrepreneurUserId") Long entrepreneurUserId, Pageable pageable);

    /**
     * 根据项目ID查找问答记录
     *
     * @param projectId 项目ID
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.project.id = :projectId ORDER BY q.sentAt DESC")
    Page<QaRecord> findByProjectIdOrderBySentAtDesc(@Param("projectId") Long projectId, Pageable pageable);

    /**
     * 根据状态查找问答记录
     *
     * @param status 问题状态
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    Page<QaRecord> findByQuestionStatusOrderBySentAtDesc(QuestionStatus status, Pageable pageable);

    /**
     * 查找融资用户的待回复问题
     *
     * @param entrepreneurUserId 融资用户ID
     * @param status 问题状态
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.entrepreneurUser.id = :entrepreneurUserId AND q.questionStatus = :status ORDER BY q.sentAt DESC")
    Page<QaRecord> findByEntrepreneurUserIdAndQuestionStatusOrderBySentAtDesc(
            @Param("entrepreneurUserId") Long entrepreneurUserId,
            @Param("status") QuestionStatus status, Pageable pageable);

    /**
     * 查找公开的问答记录
     *
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    Page<QaRecord> findByIsPublicTrueOrderBySentAtDesc(Pageable pageable);

    /**
     * 查找项目的公开问答记录
     *
     * @param projectId 项目ID
     * @param isPublic 是否公开
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.project.id = :projectId AND q.isPublic = :isPublic ORDER BY q.sentAt DESC")
    Page<QaRecord> findByProjectIdAndIsPublicOrderBySentAtDesc(
            @Param("projectId") Long projectId, @Param("isPublic") Boolean isPublic, Pageable pageable);

    /**
     * 统计待回复问题数量
     *
     * @param entrepreneurUserId 融资用户ID
     * @param status 问题状态
     * @return 数量
     */
    @Query("SELECT COUNT(q) FROM QaRecord q WHERE q.entrepreneurUser.id = :entrepreneurUserId AND q.questionStatus = :status")
    Long countByEntrepreneurUserIdAndQuestionStatus(@Param("entrepreneurUserId") Long entrepreneurUserId, @Param("status") QuestionStatus status);

    /**
     * 搜索问答记录
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.question LIKE %:keyword% OR q.answer LIKE %:keyword%")
    Page<QaRecord> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查找来自问题库的问答记录
     *
     * @param questionLibraryId 问题库ID
     * @return 问答记录列表
     */
    List<QaRecord> findByQuestionLibraryId(Long questionLibraryId);

    /**
     * 根据提问人ID查找问答记录
     *
     * @param questionerId 提问人ID
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.investorUser.id = :questionerId ORDER BY q.createdAt DESC")
    Page<QaRecord> findByQuestionerId(@Param("questionerId") Long questionerId, Pageable pageable);

    /**
     * 根据融资用户ID查找问答记录（简化方法名）
     *
     * @param entrepreneurId 融资用户ID
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.entrepreneurUser.id = :entrepreneurId ORDER BY q.createdAt DESC")
    Page<QaRecord> findByEntrepreneurId(@Param("entrepreneurId") Long entrepreneurId, Pageable pageable);

    /**
     * 根据融资用户ID和状态查找问答记录
     *
     * @param entrepreneurId 融资用户ID
     * @param status 问题状态
     * @param pageable 分页参数
     * @return 问答记录分页列表
     */
    @Query("SELECT q FROM QaRecord q WHERE q.entrepreneurUser.id = :entrepreneurId AND q.questionStatus = :status ORDER BY q.createdAt DESC")
    Page<QaRecord> findByEntrepreneurIdAndStatus(@Param("entrepreneurId") Long entrepreneurId, @Param("status") QuestionStatus status, Pageable pageable);
}
