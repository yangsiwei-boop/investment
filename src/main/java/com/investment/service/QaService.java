package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.qa.AnswerRequest;
import com.investment.dto.request.qa.QuestionSendRequest;
import com.investment.dto.request.qa.QaReplyRequest;
import com.investment.dto.response.qa.QaRecordResponse;
import com.investment.dto.response.qa.QaReplyResponse;
import com.investment.entity.*;
import com.investment.enums.QuestionStatus;
import com.investment.repository.ProjectRepository;
import com.investment.repository.QaRecordRepository;
import com.investment.repository.QaReplyRepository;
import com.investment.repository.TeaserRepository;
import com.investment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 问答服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaService {

    private final QaRecordRepository qaRecordRepository;
    private final QaReplyRepository qaReplyRepository;
    private final ProjectRepository projectRepository;
    private final TeaserRepository teaserRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /**
     * 发送问题（投资人）
     *
     * @param investorId 投资人ID
     * @param request    请求
     * @return 问答记录
     */
    @Transactional
    public QaRecordResponse sendQuestion(Long investorId, QuestionSendRequest request) {
        log.info("Sending question from investor: {}, teaser: {}", investorId, request.getTeaserId());

        User investor = userRepository.findById(investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 校验 teaser
        if (request.getTeaserId() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "teaserId不能为空");
        }
        Teaser teaser = teaserRepository.findById(request.getTeaserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEASER_NOT_FOUND));

        // 获取关联项目
        if (teaser.getProject() == null) {
            throw new BusinessException(ErrorCode.TEASER_NOT_AVAILABLE, "Teaser未关联项目");
        }
        Project project = projectRepository.findById(teaser.getProject().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        // 获取融资方
        User entrepreneur = project.getEntrepreneurUser();
        if (entrepreneur == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "项目未关联融资方用户");
        }

        QaRecord qaRecord = QaRecord.builder()
                .project(project)
                .investorUser(investor)
                .entrepreneurUser(entrepreneur)
                .question(request.getQuestion())
                .isPublic(request.getIsPublic())
                .questionStatus(QuestionStatus.PENDING)
                .sentAt(LocalDateTime.now())
                .build();

        qaRecordRepository.save(qaRecord);

        // 发送通知给融资方
        notificationService.createNotification(
                entrepreneur.getId(),
                com.investment.enums.NotificationType.NEW_QUESTION,
                "您收到一个新的问题",
                "投资人对您的项目提出了问题",
                qaRecord.getId(),
                "QA"
        );

        return convertToResponse(qaRecord);
    }

    /**
     * 回答问题（融资用户）— 创建回复记录
     *
     * @param qaId           问答ID
     * @param entrepreneurId 融资用户ID
     * @param request        请求
     * @return 问答记录
     */
    @Transactional
    public QaRecordResponse answerQuestion(Long qaId, Long entrepreneurId, AnswerRequest request) {
        log.info("Answering question: {}, entrepreneur: {}", qaId, entrepreneurId);

        QaRecord qaRecord = qaRecordRepository.findById(qaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QA_RECORD_NOT_FOUND));

        if (!qaRecord.getEntrepreneurUser().getId().equals(entrepreneurId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        User entrepreneur = userRepository.findById(entrepreneurId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 创建回复记录
        QaReply reply = QaReply.builder()
                .qaRecord(qaRecord)
                .user(entrepreneur)
                .content(request.getAnswer())
                .isPublic(request.getIsPublic())
                .build();
        qaReplyRepository.save(reply);

        // 更新问答状态和时间
        qaRecord.setAnsweredAt(LocalDateTime.now());
        qaRecord.setQuestionStatus(QuestionStatus.ANSWERED);
        qaRecordRepository.save(qaRecord);

        // 通知投资人
        notificationService.createNotification(
                qaRecord.getInvestorUser().getId(),
                com.investment.enums.NotificationType.QUESTION_ANSWERED,
                "您的问题已被回答",
                "您对项目\"" + qaRecord.getProject().getProjectName() + "\"的问题已被回答",
                qaRecord.getId(),
                "QA"
        );

        return convertToResponse(qaRecord);
    }

    /**
     * 投资人追问/追加消息
     *
     * @param qaId        问答ID
     * @param investorId  投资人ID
     * @param request     请求
     * @return 问答记录
     */
    @Transactional
    public QaRecordResponse followUpQuestion(Long qaId, Long investorId, QaReplyRequest request) {
        log.info("Investor follow-up on question: {}, investor: {}", qaId, investorId);

        QaRecord qaRecord = qaRecordRepository.findById(qaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QA_RECORD_NOT_FOUND));

        if (!qaRecord.getInvestorUser().getId().equals(investorId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        User investor = userRepository.findById(investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        QaReply reply = QaReply.builder()
                .qaRecord(qaRecord)
                .user(investor)
                .content(request.getContent())
                .isPublic(request.getIsPublic())
                .build();
        qaReplyRepository.save(reply);

        // 更新状态为待回复
        qaRecord.setQuestionStatus(QuestionStatus.PENDING);
        qaRecordRepository.save(qaRecord);

        // 通知融资方
        notificationService.createNotification(
                qaRecord.getEntrepreneurUser().getId(),
                com.investment.enums.NotificationType.NEW_QUESTION,
                "投资人追加了问题",
                "投资人对项目\"" + qaRecord.getProject().getProjectName() + "\"追加了问题",
                qaRecord.getId(),
                "QA"
        );

        return convertToResponse(qaRecord);
    }

    /**
     * 获取投资人的问题列表
     */
    public Page<QaRecordResponse> getInvestorQuestions(Long investorId, int page, int size) {
        log.info("Getting questions for investor: {}", investorId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QaRecord> qaPage = qaRecordRepository.findByQuestionerId(investorId, pageable);

        return qaPage.map(this::convertToResponse);
    }

    /**
     * 获取融资用户收到的问答列表
     */
    public Page<QaRecordResponse> getEntrepreneurQaRecords(Long entrepreneurId, String status, int page, int size) {
        log.info("Getting QA records for entrepreneur: {}", entrepreneurId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<QaRecord> qaPage;
        if (status != null && !status.isEmpty()) {
            qaPage = qaRecordRepository.findByEntrepreneurIdAndStatus(entrepreneurId, QuestionStatus.valueOf(status), pageable);
        } else {
            qaPage = qaRecordRepository.findByEntrepreneurId(entrepreneurId, pageable);
        }

        return qaPage.map(this::convertToResponse);
    }

    /**
     * 获取问答详情
     */
    public QaRecordResponse getQaDetail(Long qaId, Long userId) {
        QaRecord qaRecord = qaRecordRepository.findById(qaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QA_RECORD_NOT_FOUND));

        return convertToResponse(qaRecord);
    }

    /**
     * 转换为响应DTO（包含所有回复）
     */
    private QaRecordResponse convertToResponse(QaRecord qa) {
        Project project = qa.getProject();
        User questioner = qa.getInvestorUser();
        User answerer = qa.getEntrepreneurUser();

        // 查询所有回复
        List<QaReply> replies = qaReplyRepository.findByQaRecordIdOrderByCreatedAtAsc(qa.getId());
        List<QaReplyResponse> replyResponses = replies.stream()
                .map(this::convertToReplyResponse)
                .toList();

        // 取最新回复作为兼容字段
        QaReplyResponse latestReply = replyResponses.isEmpty() ? null
                : replyResponses.get(replyResponses.size() - 1);

        return QaRecordResponse.builder()
                .id(qa.getId())
                .teaserId(project != null ? project.getId() : null)
                .teaserTitle(project != null ? project.getProjectName() : null)
                .projectName(project != null ? project.getProjectName() : null)
                .questionerId(questioner != null ? questioner.getId() : null)
                .questionerName(questioner != null ? questioner.getRealName() : null)
                .question(qa.getQuestion())
                .answer(latestReply != null ? latestReply.getContent() : null)
                .answererId(latestReply != null ? latestReply.getUserId() : null)
                .answererName(latestReply != null ? latestReply.getUserName() : null)
                .status(qa.getQuestionStatus() != null ? qa.getQuestionStatus().name() : null)
                .isPublic(qa.getIsPublic())
                .questionedAt(qa.getSentAt())
                .answeredAt(qa.getAnsweredAt())
                .replyCount(replyResponses.size())
                .replies(replyResponses)
                .createdAt(qa.getCreatedAt())
                .build();
    }

    /**
     * 转换回复为响应DTO
     */
    private QaReplyResponse convertToReplyResponse(QaReply reply) {
        User user = reply.getUser();
        return QaReplyResponse.builder()
                .id(reply.getId())
                .qaRecordId(reply.getQaRecord().getId())
                .userId(user != null ? user.getId() : null)
                .userName(user != null ? user.getRealName() : null)
                .userType(user != null ? user.getUserType().name() : null)
                .content(reply.getContent())
                .isPublic(reply.getIsPublic())
                .createdAt(reply.getCreatedAt())
                .build();
    }
}
