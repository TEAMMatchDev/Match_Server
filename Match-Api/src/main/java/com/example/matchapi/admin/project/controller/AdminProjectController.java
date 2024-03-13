package com.example.matchapi.admin.project.controller;

import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.annotation.Enum;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.errorcode.FileUploadException;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.project.entity.enums.ProjectStatus;
import com.example.matchdomain.project.exception.PatchProjectImageErrorCode;
import com.example.matchdomain.project.exception.ProjectGetErrorCode;
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
import javax.validation.constraints.Min;
import java.util.List;

import static com.example.matchcommon.exception.errorcode.FileUploadException.FILE_UPLOAD_NOT_EMPTY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/projects")
@Tag(name = "ADMIN-03-Project💻 관리자 프로젝트 관련 API 입니다.", description = "프로젝트 관리 API 입니다.")
public class AdminProjectController {
    private final ProjectService projectService;
    @Operation(summary = "ADMIN-03-01💻 프로젝트 리스트 업로드 API.",description = "프로젝트 업로드 API 입니다.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class, FileUploadException.class})
    public CommonResponse<String> postProject(
            @Valid @RequestPart("project") ProjectReq.Project project,
            @RequestPart("presentFile") MultipartFile presentFile,
            @RequestPart("multipartFiles") List<MultipartFile> multipartFiles){
        if(presentFile.isEmpty()) throw new BadRequestException(FILE_UPLOAD_NOT_EMPTY);
        if(multipartFiles.isEmpty()) throw new BadRequestException(FILE_UPLOAD_NOT_EMPTY);
        projectService.postProject(project, presentFile, multipartFiles);
        return CommonResponse.onSuccess("프로젝트 업로드 성공");
    }

    @GetMapping("")
    @Operation(summary = "ADMIN-03-02💻 프로젝트 리스트 조회 API.",description = "프로젝트 리스트 조회 API 입니다.")
    @ApiErrorCodeExample({UserAuthErrorCode.class,ProjectGetErrorCode.class})
    public CommonResponse<PageResponse<List<ProjectRes.ProjectAdminList>>> getProjectList(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = true, defaultValue = "0") @Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true, defaultValue = "10") int size
    ){
        PageResponse<List<ProjectRes.ProjectAdminList>> projectList = projectService.getProjectList(page,size);

        return CommonResponse.onSuccess(projectList);
    }

    @GetMapping("/donation-users/{projectId}")
    @Operation(summary = "ADMIN-03-03-01💻 프로젝트 기부자 리스트 조회 API.",description = "프로젝트 리스트 조회 API 입니다.")
    @ApiErrorCodeExample({UserAuthErrorCode.class, ProjectGetErrorCode.class})
    public CommonResponse<PageResponse<List<ProjectRes.DonationList>>> getDonationList(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = true, defaultValue = "0") @Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true, defaultValue = "10") int size,
            @PathVariable Long projectId){
        PageResponse<List<ProjectRes.DonationList>> donationList = projectService.getDonationList(projectId, page, size);

        return CommonResponse.onSuccess(donationList);
    }


    @GetMapping("/{projectId}")
    @Operation(summary = "ADMIN-03-03💻 프로젝트 상세 조회 API.",description = "프로젝트 상세 조회 API 입니다.")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    public CommonResponse<ProjectRes.ProjectAdminDetail> getProjectDetail(@PathVariable Long projectId){
        ProjectRes.ProjectAdminDetail projectDetail = projectService.getProjectAdminDetail(projectId);

        return CommonResponse.onSuccess(projectDetail);
    }


    @Operation(summary = "ADMIN-03-04💻 프로젝트 기부 상태 수정.",description = "프로젝트 기부상태 수정 API 입니다.")
    @PatchMapping(value = "/project-status/{projectId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class, ProjectGetErrorCode.class})
    public CommonResponse<String> patchProjectStatus(@Enum(message = "enum에 일치하는 값이 존재하지 않습니다.")  @RequestParam ProjectStatus projectStatus, @PathVariable Long projectId){
        projectService.patchProjectStatus(projectStatus, projectId);
        return CommonResponse.onSuccess("프로젝트 수정 성공");
    }

    @Operation(summary = "ADMIN-03-05💻 프로젝트 삭제.",description = "프로젝트 삭제 API 입니다.")
    @DeleteMapping("/{projectId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, ProjectGetErrorCode.class})
    public CommonResponse<String> deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return CommonResponse.onSuccess("삭제 성공");
    }

    @Operation(summary = "ADMIN-03-05-01💻 프로젝트 삭제 복구.",description = "프로젝트 삭제 복구 API 입니다.")
    @PatchMapping("/activation/{projectId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, ProjectGetErrorCode.class})
    public CommonResponse<String> patchActiveProject(@PathVariable Long projectId){
        projectService.patchProjectActive(projectId);
        return CommonResponse.onSuccess("삭제 성공");
    }

    @Operation(summary = "ADMIN-03-06💻 프로젝트 글 수정.",description = "프로젝트 글 수정 API 입니다.")
    @RequestMapping(value = "/modify/{projectId}", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    @ApiErrorCodeExample({UserAuthErrorCode.class, ProjectGetErrorCode.class})
    public CommonResponse<String> patchProject(@PathVariable Long projectId,
        @RequestPart ProjectReq.ModifyProject modifyProject,
        @RequestPart(value = "presentFile", required = false) MultipartFile presentFile,
        @RequestPart(value = "multipartFiles", required = false) List<MultipartFile> multipartFiles){
        projectService.patchProject(projectId, modifyProject, presentFile, multipartFiles);
        return CommonResponse.onSuccess("수정 성공");
    }


}
