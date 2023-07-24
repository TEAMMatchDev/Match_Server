package com.example.matchapi.project.controller;

import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "03-Project💻", description = "프로젝트 모아보기 용 API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    @GetMapping("")
    public CommonResponse<PageResponse<List<ProjectRes.ProjectList>>> getProjectList(@Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) int page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) int size) {
        return CommonResponse.onSuccess(projectService.getProjectList(page, size));
    }
}
