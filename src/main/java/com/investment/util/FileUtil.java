package com.investment.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @author Investment Team
 */
@Slf4j
public class FileUtil {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    );

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名（小写）
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 生成唯一文件名
     *
     * @param originalFileName 原始文件名
     * @return 新文件名
     */
    public static String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 生成带日期的文件路径
     *
     * @param basePath 基础路径
     * @return 完整路径
     */
    public static String generateDatePath(String basePath) {
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return basePath + "/" + datePath;
    }

    /**
     * 检查文件类型是否为图片
     *
     * @param fileName 文件名
     * @return 是否为图片
     */
    public static boolean isImage(String fileName) {
        String extension = getFileExtension(fileName);
        return ALLOWED_IMAGE_TYPES.contains(extension);
    }

    /**
     * 检查文件类型是否为文档
     *
     * @param fileName 文件名
     * @return 是否为文档
     */
    public static boolean isDocument(String fileName) {
        String extension = getFileExtension(fileName);
        return ALLOWED_DOCUMENT_TYPES.contains(extension);
    }

    /**
     * 检查文件大小是否合法
     *
     * @param fileSize 文件大小
     * @return 是否合法
     */
    public static boolean isFileSizeValid(long fileSize) {
        return fileSize > 0 && fileSize <= MAX_FILE_SIZE;
    }

    /**
     * 保存文件
     *
     * @param file     文件
     * @param filePath 文件路径
     * @return 保存的文件路径
     * @throws IOException IO异常
     */
    public static String saveFile(MultipartFile file, String filePath) throws IOException {
        Path directory = Paths.get(filePath);
        Files.createDirectories(directory);

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        Path targetPath = directory.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return targetPath.toString();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    /**
     * 获取文件大小（人类可读格式）
     *
     * @param size 文件大小（字节）
     * @return 格式化的大小
     */
    public static String getReadableFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * 获取文件MIME类型
     *
     * @param fileName 文件名
     * @return MIME类型
     */
    public static String getMimeType(String fileName) {
        String extension = getFileExtension(fileName);
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls" -> "application/vnd.ms-excel";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt" -> "application/vnd.ms-powerpoint";
            case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            default -> "application/octet-stream";
        };
    }
}
