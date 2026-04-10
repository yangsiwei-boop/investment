package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:52428800}")
    private long maxFileSize;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    );

    /**
     * 上传图片
     *
     * @param file 文件
     * @param dir  子目录
     * @return 文件路径
     */
    public String uploadImage(MultipartFile file, String dir) {
        validateImageFile(file);
        return saveFile(file, "images/" + dir);
    }

    /**
     * 上传文档
     *
     * @param file 文件
     * @param dir  子目录
     * @return 文件路径
     */
    public String uploadDocument(MultipartFile file, String dir) {
        validateDocumentFile(file);
        return saveFile(file, "documents/" + dir);
    }

    /**
     * 上传头像
     *
     * @param file 文件
     * @return 文件路径
     */
    public String uploadAvatar(MultipartFile file) {
        return uploadImage(file, "avatars");
    }

    /**
     * 上传BP
     *
     * @param file      文件
     * @param projectId 项目ID
     * @return 文件路径
     */
    public String uploadBp(MultipartFile file, Long projectId) {
        return uploadDocument(file, "bp/" + projectId);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    /**
     * 获取文件URL
     *
     * @param filePath 文件路径
     * @return 文件URL
     */
    public String getFileUrl(String filePath) {
        // 返回可通过 WebMvcConfig 静态资源映射访问的 URL 路径
        return "/uploads/" + filePath;
    }

    /**
     * 验证图片文件
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_IMAGE_TYPES.contains(extension.toLowerCase())) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    /**
     * 验证文档文件
     */
    private void validateDocumentFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_DOCUMENT_TYPES.contains(extension.toLowerCase())) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    /**
     * 保存文件
     */
    private String saveFile(MultipartFile file, String dir) {
        try {
            // 创建上传目录（兼容 Windows/Linux）
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path directory = Paths.get(uploadPath, dir, datePath);
            Files.createDirectories(directory);

            // 生成文件名
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName);
            String newFileName = UUID.randomUUID().toString() + "." + extension;

            // 保存文件
            Path filePath = directory.resolve(newFileName);
            file.transferTo(filePath);

            // 返回相对路径（从 uploadPath 开始），兼容不同操作系统
            Path basePath = Paths.get(uploadPath).toAbsolutePath();
            return basePath.relativize(filePath.toAbsolutePath()).toString().replace('\\', '/');
        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
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
}
