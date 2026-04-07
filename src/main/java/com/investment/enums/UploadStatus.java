package com.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传状态枚举
 *
 * @author Investment Team
 */
@Getter
@AllArgsConstructor
public enum UploadStatus {
    UPLOADING("uploading", "上传中"),
    COMPLETED("completed", "上传完成"),
    FAILED("failed", "上传失败");

    private final String code;
    private final String description;
}
