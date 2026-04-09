package com.investment.repository;

import com.investment.entity.QaReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问答回复数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface QaReplyRepository extends JpaRepository<QaReply, Long> {

    /**
     * 按时间升序获取某个问答的所有回复
     *
     * @param qaRecordId 问答记录ID
     * @return 回复列表
     */
    List<QaReply> findByQaRecordIdOrderByCreatedAtAsc(Long qaRecordId);

    /**
     * 统计某个问答的回复数量
     *
     * @param qaRecordId 问答记录ID
     * @return 回复数量
     */
    long countByQaRecordId(Long qaRecordId);
}
