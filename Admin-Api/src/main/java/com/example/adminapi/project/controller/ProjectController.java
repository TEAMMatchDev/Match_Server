package com.example.adminapi.project.controller;

import com.example.adminapi.project.dto.ProjectReq;
import com.example.adminapi.project.service.ProjectService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.project.entity.RegularStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Tag(name = "03-Project💻", description = "프로젝트 관리r API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    @Operation(summary = "03-01💻 프로젝트 리스트 조회 API.",description = "프로젝트 리스트 조회 API 입니다.")
    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public CommonResponse<String> postProject(
            @ModelAttribute("project") ProjectReq.Project project,
            @ModelAttribute("presentFile") MultipartFile presentFile,
            @ModelAttribute("multipartFiles") List<MultipartFile> multipartFiles){
        projectService.postProject(project, presentFile, multipartFiles);
        return CommonResponse.onSuccess("프로젝트 업로드 성공");
    }
}
