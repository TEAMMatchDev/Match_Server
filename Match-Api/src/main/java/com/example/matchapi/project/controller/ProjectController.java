package com.example.matchapi.project.controller;

import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchdomain.project.exception.ProjectErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "03-Project💻", description = "프로젝트 모아보기 용 API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    @Operation(summary = "프로젝트 리스트 조회 API.",description = "프로젝트 리스트 조회 API 입니다.")
    @GetMapping("")
    public CommonResponse<PageResponse<List<ProjectRes.ProjectList>>> getProjectList(@Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) int page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) int size) {
        return CommonResponse.onSuccess(projectService.getProjectList(page, size));
    }

    @Operation(summary = "프로젝트 상세조회 API.",description = "프로젝트 상세조회 API 입니다.")
    @GetMapping("/{projectId}")
    @ApiErrorCodeExample({ProjectErrorCode.class})
    public CommonResponse<ProjectRes.ProjectDetail> getProject(@Parameter(description = "프로젝트 ID", example = "1")
                                                     @PathVariable(required = true) Long projectId) {
        return CommonResponse.onSuccess(projectService.getProjectDetail(projectId));
    }
}
