package com.investment.controller;

import com.investment.common.response.ApiResponse;
import com.investment.dto.request.entrepreneur.ProjectCreateRequest;
import com.investment.dto.response.entrepreneur.ProjectResponse;
import com.investment.security.UserPrincipal;
import com.investment.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 融资用户项目管理控制器
 *
 * @author Investment Team
 */
@Slf4j
@RestController
@RequestMapping("/entrepreneur/projects")
@RequiredArgsConstructor
@Tag(name = "融资用户-项目管理", description = "融资用户项目管理相关接口")
@SecurityRequirement(name = "Bearer")
public class EntrepreneurProjectController {

    private final ProjectService projectService;

    /**
     * 创建项目
     *
     * @param request   创建请求
     * @param principal 当前用户
     * @return 项目信息
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建项目", description = "创建新的融资项目")
    public ApiResponse<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Creating project for entrepreneur: {}", principal.getId());

        ProjectResponse response = projectService.createProject(principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 获取项目列表
     *
     * @param page      页码
     * @param size      每页数量
     * @param principal 当前用户
     * @return 项目列表
     */
    @GetMapping
    @Operation(summary = "获取项目列表", description = "获取融资用户的项目列表")
    public ApiResponse<Page<ProjectResponse>> getProjectList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting project list for entrepreneur: {}", principal.getId());

        Page<ProjectResponse> projects = projectService.getProjectList(principal.getId(), page, size);
        return ApiResponse.success(projects);
    }

    /**
     * 获取项目详情
     *
     * @param projectId 项目ID
     * @param principal 当前用户
     * @return 项目详情
     */
    @GetMapping("/{projectId}")
    @Operation(summary = "获取项目详情", description = "获取指定项目的详细信息")
    public ApiResponse<ProjectResponse> getProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Getting project: {}, entrepreneur: {}", projectId, principal.getId());

        ProjectResponse response = projectService.getProject(projectId, principal.getId());
        return ApiResponse.success(response);
    }

    /**
     * 更新项目
     *
     * @param projectId 项目ID
     * @param request   更新请求
     * @param principal 当前用户
     * @return 项目信息
     */
    @PutMapping("/{projectId}")
    @Operation(summary = "更新项目", description = "更新项目信息")
    public ApiResponse<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Updating project: {}, entrepreneur: {}", projectId, principal.getId());

        ProjectResponse response = projectService.updateProject(projectId, principal.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 删除项目
     *
     * @param projectId 项目ID
     * @param principal 当前用户
     * @return 成功响应
     */
    @DeleteMapping("/{projectId}")
    @Operation(summary = "删除项目", description = "删除指定项目")
    public ApiResponse<Void> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Deleting project: {}, entrepreneur: {}", projectId, principal.getId());

        projectService.deleteProject(projectId, principal.getId());
        return ApiResponse.success(null);
    }
}
