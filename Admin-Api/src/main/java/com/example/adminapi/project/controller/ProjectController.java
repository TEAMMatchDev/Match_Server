package com.example.adminapi.project.controller;

import com.example.adminapi.project.dto.ProjectReq;
import com.example.adminapi.project.service.ProjectService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.errorcode.FileUploadException;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.example.matchcommon.exception.errorcode.FileUploadException.FILE_UPLOAD_NOT_EMPTY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Tag(name = "03-Project💻", description = "프로젝트 관리r API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    @Operation(summary = "03-01💻 프로젝트 리스트 조회 API.",description = "프로젝트 리스트 조회 API 입니다.")
    @PostMapping(value = "", consumes = {"multipart/form-data"}, produces = "application/json")
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class, FileUploadException.class})
    public CommonResponse<String> postProject(
            @Parameter(description = "project 상세 내용입니다. type 은 application/json"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid @RequestPart("project") ProjectReq.Project project,
            @RequestPart("presentFile") MultipartFile presentFile,
            @RequestPart("multipartFiles") List<MultipartFile> multipartFiles){
        if(!presentFile.isEmpty()||!multipartFiles.isEmpty()) throw new BadRequestException(FILE_UPLOAD_NOT_EMPTY);
        projectService.postProject(project, presentFile, multipartFiles);
        return CommonResponse.onSuccess("프로젝트 업로드 성공");
    }

    /*
    @Operation(summary = "03-02💻 프로젝트 상태 수정.",description = "프로젝트 리스트 조회 API 입니다.")
    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class, FileUploadException.class})
    public CommonResponse<String> patchProjectStatus(@Valid @RequestBody ProjectReq.ModifyProjectStatus modifyProjectStatus){
        return CommonResponse.onSuccess("프로젝트 업로드 성공");
    }

     */
}
