package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.response.entrepreneur.BusinessPlanResponse;
import com.investment.entity.BusinessPlan;
import com.investment.entity.Project;
import com.investment.enums.UploadStatus;
import com.investment.repository.BusinessPlanRepository;
import com.investment.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 商业计划书服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessPlanService {

    private final BusinessPlanRepository businessPlanRepository;
    private final ProjectRepository projectRepository;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:52428800}")
    private long maxFileSize;

    /**
     * 上传商业计划书
     *
     * @param projectId 项目ID
     * @param userId    用户ID
     * @param file      文件
     * @return BP响应
     */
    @Transactional
    public BusinessPlanResponse uploadBusinessPlan(Long projectId, Long userId, MultipartFile file) {
        log.info("Uploading BP for project: {}, user: {}", projectId, userId);

        // 验证项目
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 验证文件
        validateFile(file);

        // 保存文件
        String fileUrl = saveFile(file, projectId);

        // 创建BP记录
        BusinessPlan bp = BusinessPlan.builder()
                .project(project)
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .fileSize((int) file.getSize())
                .fileFormat(getFileExtension(file.getOriginalFilename()))
                .uploadStatus(UploadStatus.COMPLETED)
                .build();

        businessPlanRepository.save(bp);

        return convertToResponse(bp, project.getProjectName());
    }

    /**
     * 获取项目的BP列表
     *
     * @param projectId 项目ID
     * @param userId    用户ID
     * @return BP列表
     */
    public List<BusinessPlanResponse> getBusinessPlanList(Long projectId, Long userId) {
        log.info("Getting BP list for project: {}, user: {}", projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        List<BusinessPlan> bpList = businessPlanRepository.findByProjectIdOrderByCreatedAtDesc(projectId);

        return bpList.stream()
                .map(bp -> convertToResponse(bp, project.getProjectName()))
                .toList();
    }

    /**
     * 获取BP详情
     *
     * @param bpId   BP ID
     * @param userId 用户ID
     * @return BP响应
     */
    public BusinessPlanResponse getBusinessPlan(Long bpId, Long userId) {
        BusinessPlan bp = businessPlanRepository.findById(bpId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUSINESS_PLAN_NOT_FOUND));

        Project project = bp.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        return convertToResponse(bp, project.getProjectName());
    }

    /**
     * 删除BP
     *
     * @param bpId   BP ID
     * @param userId 用户ID
     */
    @Transactional
    public void deleteBusinessPlan(Long bpId, Long userId) {
        log.info("Deleting BP: {}, user: {}", bpId, userId);

        BusinessPlan bp = businessPlanRepository.findById(bpId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUSINESS_PLAN_NOT_FOUND));

        Project project = bp.getProject();
        if (project == null || !project.getEntrepreneurUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        // 删除文件
        try {
            if (bp.getFileUrl() != null) {
                Files.deleteIfExists(Paths.get(bp.getFileUrl()));
            }
        } catch (IOException e) {
            log.error("Failed to delete BP file: {}", bp.getFileUrl(), e);
        }

        // 删除记录
        businessPlanRepository.delete(bp);
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!isValidExtension(extension)) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    /**
     * 验证文件扩展名
     */
    private boolean isValidExtension(String extension) {
        if (extension == null) {
            return false;
        }
        String ext = extension.toLowerCase();
        return ext.equals("pdf") || ext.equals("doc") || ext.equals("docx")
                || ext.equals("ppt") || ext.equals("pptx");
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 保存文件
     */
    private String saveFile(MultipartFile file, Long projectId) {
        try {
            // 创建上传目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String dirPath = uploadPath + "/bp/" + projectId + "/" + datePath;
            Path directory = Paths.get(dirPath);
            Files.createDirectories(directory);

            // 生成文件名
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName);
            String newFileName = UUID.randomUUID().toString() + "." + extension;

            // 保存文件
            Path filePath = directory.resolve(newFileName);
            file.transferTo(filePath.toFile());

            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    /**
     * 转换为响应DTO
     */
    private BusinessPlanResponse convertToResponse(BusinessPlan bp, String projectName) {
        return BusinessPlanResponse.builder()
                .id(bp.getId())
                .projectId(bp.getProject() != null ? bp.getProject().getId() : null)
                .projectName(projectName)
                .fileName(bp.getFileName())
                .fileUrl(bp.getFileUrl())
                .fileSize(bp.getFileSize() != null ? bp.getFileSize().longValue() : null)
                .fileFormat(bp.getFileFormat())
                .uploadStatus(bp.getUploadStatus() != null ? bp.getUploadStatus().name() : null)
                .createdAt(bp.getCreatedAt())
                .build();
    }
}
