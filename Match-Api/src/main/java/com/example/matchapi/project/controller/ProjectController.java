package com.example.matchapi.project.controller;

import com.example.matchapi.common.aop.CheckIdExist;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "03-Project💻", description = "프로젝트 모아보기 용 API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    @Operation(summary = "03-01💻 프로젝트 리스트 조회 API.",description = "프로젝트 리스트 조회 API 입니다.")
    @GetMapping("")
    public CommonResponse<PageResponse<List<ProjectRes.ProjectList>>> getProjectList(@Parameter(description = "페이지", example = "0") @RequestParam(required = true, defaultValue = "0") @Min(value = 0) int page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true, defaultValue = "10") int size) {
        log.info("03-01 프로젝트 리스트 조회");
        return CommonResponse.onSuccess(projectService.getProjectList(page, size));
    }

    @Operation(summary = "03-02💻 프로젝트 상세조회 API.",description = "프로젝트 상세조회 API 입니다.")
    @GetMapping("/{projectId}")
    @CheckIdExist
    @ApiErrorCodeExample({ProjectErrorCode.class})
    public CommonResponse<ProjectRes.ProjectDetail> getProject(@Parameter(description = "프로젝트 ID", example = "1")
                                                                   @PathVariable(required = true)Long projectId) {
        log.info("03-02 프로젝트 상세 조회 projectId : "+projectId);
        return CommonResponse.onSuccess(projectService.getProjectDetail(projectId));
    }


    @Operation(summary = "03-03💻 프로젝트 검색 조회",description = "프로젝트 검색 조회 API 입니다.")
    @GetMapping("/search")
    public CommonResponse<PageResponse<List<ProjectRes.ProjectList>>> searchProjectList(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = true, defaultValue = "0") @Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true, defaultValue = "10") int size,
            @Parameter(description = "검색어")  @RequestParam("content") String content
            ){
        log.info("03-03 프로젝트 검색 조회 projectId : "+ content);
        return CommonResponse.onSuccess(projectService.searchProjectList(content, page, size));
    }

    @Operation(summary = "03-03💻 프로젝트 검색 조회",description = "프로젝트 검색 조회 API 입니다.")
    @GetMapping("/comment/{projectId}")
    public CommonResponse<PageResponse<List<ProjectRes.CommentList>>> getProjectComment(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = true, defaultValue = "0") @Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true, defaultValue = "10") int size,
            @Parameter(description = "검색어")  @PathVariable("projectId") Long projectId
    ){
        log.info("03-04 프로젝트 댓글 조회 projectId : "+ projectId);
        return CommonResponse.onSuccess(projectService.getProjectComment(projectId, page, size));
    }



}
