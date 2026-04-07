package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.response.common.FileUploadResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用文件上传控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/common/upload")
@RequiredArgsConstructor
@Tag(name = "通用-文件上传", description = "文件上传相关接口")
@SecurityRequirement(name = "Bearer")
public class CommonController {

    private final FileUploadService fileUploadService;

    /**
     * 上传图片
     *
     * @param file      文件
     * @param dir       子目录
     * @param principal 当前用户
     * @return 文件信息
     */
    @PostMapping("/image")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "上传图片", description = "上传图片文件")
    public ApiResponse<FileUploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", defaultValue = "common") String dir,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Uploading image for user: {}", principal.getId());

        String filePath = fileUploadService.uploadImage(file, dir);

        FileUploadResponse response = FileUploadResponse.builder()
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .fileUrl(fileUploadService.getFileUrl(filePath))
                .fileSize(file.getSize())
                .fileType(getFileExtension(file.getOriginalFilename()))
                .build();

        return ApiResponse.success(response);
    }

    /**
     * 上传文档
     *
     * @param file      文件
     * @param dir       子目录
     * @param principal 当前用户
     * @return 文件信息
     */
    @PostMapping("/document")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "上传文档", description = "上传文档文件")
    public ApiResponse<FileUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", defaultValue = "common") String dir,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Uploading document for user: {}", principal.getId());

        String filePath = fileUploadService.uploadDocument(file, dir);

        FileUploadResponse response = FileUploadResponse.builder()
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .fileUrl(fileUploadService.getFileUrl(filePath))
                .fileSize(file.getSize())
                .fileType(getFileExtension(file.getOriginalFilename()))
                .build();

        return ApiResponse.success(response);
    }

    /**
     * 上传头像
     *
     * @param file      文件
     * @param principal 当前用户
     * @return 文件信息
     */
    @PostMapping("/avatar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "上传头像", description = "上传用户头像")
    public ApiResponse<FileUploadResponse> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Uploading avatar for user: {}", principal.getId());

        String filePath = fileUploadService.uploadAvatar(file);

        FileUploadResponse response = FileUploadResponse.builder()
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .fileUrl(fileUploadService.getFileUrl(filePath))
                .fileSize(file.getSize())
                .fileType(getFileExtension(file.getOriginalFilename()))
                .build();

        return ApiResponse.success(response);
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
