package com.investment.entity;

import com.investment.enums.AnalysisStatus;
import com.investment.enums.UploadStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * 商业计划书实体
 *
 * @author Investment Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_plans")
public class BusinessPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 项目ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    /**
     * 文件名
     */
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    /**
     * 文件URL
     */
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    /**
     * 缩略图URL（PDF预览图）
     */
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    /**
     * 文件大小(字节)
     */
    @Column(name = "file_size")
    private Integer fileSize;

    /**
     * 页数
     */
    @Column(name = "page_count")
    private Integer pageCount;

    /**
     * 文件格式
     */
    @Column(name = "file_format", length = 10)
    private String fileFormat;

    /**
     * 文件哈希值（用于去重）
     */
    @Column(name = "file_hash", length = 64)
    private String fileHash;

    /**
     * 上传状态
     */
    @Column(name = "upload_status", length = 20)
    @Builder.Default
    private UploadStatus uploadStatus = UploadStatus.UPLOADING;

    /**
     * 上传来源
     */
    @Column(name = "upload_source", length = 20)
    @Builder.Default
    private String uploadSource = "web";

    /**
     * Teaser生成状态
     */
    @Column(name = "teaser_generation_status", length = 20)
    @Builder.Default
    private AnalysisStatus teaserGenerationStatus = AnalysisStatus.PENDING;

    /**
     * 内容提取状态
     */
    @Column(name = "extraction_status", length = 20)
    @Builder.Default
    private AnalysisStatus extractionStatus = AnalysisStatus.PENDING;

    /**
     * 提取的内容（JSON格式）
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extracted_content", columnDefinition = "json")
    private String extractedContent;

    /**
     * AI分析状态
     */
    @Column(name = "ai_analysis_status", length = 20)
    @Builder.Default
    private AnalysisStatus aiAnalysisStatus = AnalysisStatus.PENDING;

    /**
     * 关联的分析记录ID
     */
    @Column(name = "analysis_id")
    private Long analysisId;

    /**
     * 关联的Teaser ID
     */
    @Column(name = "teaser_id")
    private Long teaserId;
}
