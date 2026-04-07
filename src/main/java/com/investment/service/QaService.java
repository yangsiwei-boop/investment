package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.qa.AnswerRequest;
import com.investment.dto.request.qa.QuestionSendRequest;
import com.investment.dto.response.qa.QaRecordResponse;
import com.investment.entity.Project;
import com.investment.entity.QaRecord;
import com.investment.entity.User;
import com.investment.enums.QuestionStatus;
import com.investment.repository.ProjectRepository;
import com.investment.repository.QaRecordRepository;
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
    private final ProjectRepository projectRepository;
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

        // 获取投资人和项目（通过Teaser获取）
        User investor = userRepository.findById(investorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // TODO: 需要通过TeaserService获取Teaser及关联的Project
        // 这里暂时使用占位逻辑，实际实现需要根据Teaser获取Project
        Project project = null;
        User entrepreneur = null;

        // 创建问题
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

        // 发送通知给融资用户（如果有）
        if (entrepreneur != null) {
            notificationService.createNotification(
                    entrepreneur.getId(),
                    com.investment.enums.NotificationType.NEW_QUESTION,
                    "您收到一个新的问题",
                    "投资人对您的项目提出了问题",
                    qaRecord.getId(),
                    "QA"
            );
        }

        return convertToResponse(qaRecord);
    }

    /**
     * 回答问题（融资用户）
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

        // 验证权限
        if (!qaRecord.getEntrepreneurUser().getId().equals(entrepreneurId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 更新回答
        qaRecord.setAnswer(request.getAnswer());
        qaRecord.setIsPublic(request.getIsPublic());
        qaRecord.setQuestionStatus(QuestionStatus.ANSWERED);
        qaRecord.setAnsweredAt(LocalDateTime.now());

        qaRecordRepository.save(qaRecord);

        // 发送通知给投资人
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
     * 获取投资人的问题列表
     *
     * @param investorId 投资人ID
     * @param page       页码
     * @param size       每页数量
     * @return 问题列表
     */
    public Page<QaRecordResponse> getInvestorQuestions(Long investorId, int page, int size) {
        log.info("Getting questions for investor: {}", investorId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QaRecord> qaPage = qaRecordRepository.findByQuestionerId(investorId, pageable);

        return qaPage.map(this::convertToResponse);
    }

    /**
     * 获取融资用户收到的问答列表
     *
     * @param entrepreneurId 融资用户ID
     * @param status         状态筛选（可选）
     * @param page           页码
     * @param size           每页数量
     * @return 问答列表
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
     *
     * @param qaId   问答ID
     * @param userId 用户ID
     * @return 问答详情
     */
    public QaRecordResponse getQaDetail(Long qaId, Long userId) {
        QaRecord qaRecord = qaRecordRepository.findById(qaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QA_RECORD_NOT_FOUND));

        return convertToResponse(qaRecord);
    }

    /**
     * 转换为响应DTO
     */
    private QaRecordResponse convertToResponse(QaRecord qa) {
        Project project = qa.getProject();
        User questioner = qa.getInvestorUser();
        User answerer = qa.getEntrepreneurUser();

        return QaRecordResponse.builder()
                .id(qa.getId())
                .teaserId(project != null ? project.getId() : null)
                .teaserTitle(project != null ? project.getProjectName() : null)
                .projectName(project != null ? project.getProjectName() : null)
                .questionerId(questioner != null ? questioner.getId() : null)
                .questionerName(questioner != null ? questioner.getRealName() : null)
                .question(qa.getQuestion())
                .answer(qa.getAnswer())
                .answererId(answerer != null ? answerer.getId() : null)
                .answererName(answerer != null ? answerer.getRealName() : null)
                .status(qa.getQuestionStatus() != null ? qa.getQuestionStatus().name() : null)
                .isPublic(qa.getIsPublic())
                .questionedAt(qa.getSentAt())
                .answeredAt(qa.getAnsweredAt())
                .createdAt(qa.getCreatedAt())
                .build();
    }
}
