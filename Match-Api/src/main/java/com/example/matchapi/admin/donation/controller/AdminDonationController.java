package com.example.matchapi.admin.donation.controller;

import com.example.matchapi.admin.donation.service.AdminDonationService;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/donations")
@Tag(name = "ADMIN-05-Donation💸 관리자 기부금 관련 API 입니다.", description = "기부금 관련 API 입니다.")
@Slf4j
public class AdminDonationController {
    private final AdminDonationService adminDonationService;
    private final ProjectService projectService;
    @GetMapping("")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-01💸 기부금 현황파악 API.",description = "기부금 현황파악 API 입니다.")
    public CommonResponse<DonationRes.DonationInfo> getDonationInfo(){
        DonationRes.DonationInfo info = adminDonationService.getDonationInfo();
        return CommonResponse.onSuccess(info);
    }

    @GetMapping("/regular")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-01-01 정기 결제 현황 파악", description = "정기 결제 현황파악")
    public CommonResponse<DonationRes.RegularInfoDto> getRegularInfo(){
        return CommonResponse.onSuccess(adminDonationService.getRegularInfo());
    }


    @GetMapping("/{donationId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-02 기부금 상세조회 API", description = "기부금 상세조회 API")
    public CommonResponse<DonationRes.DonationDetail> getDonationDetail(@PathVariable Long donationId){
        return CommonResponse.onSuccess(adminDonationService.getDonationDetail(donationId));
    }

    /*@PostMapping("/complete")
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class})
    @Operation(summary = "ADMIN-05-03 기부금 집행 중 API POST API", description = "기부금 집행 중 API")
    public CommonResponse<String> postExecution(
            @RequestBody DonationReq.EnforceDonation enforceDonation
    ){
        adminDonationService.postExecution(enforceDonation);
        return CommonResponse.onSuccess("집행 중 으로 변환 성공");
    }
     */


    @PostMapping(value = "/enforce", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class})
    @Operation(summary = "ADMIN-05-04 기부금 전달완료 POST API", description = "기부금 집행 API")
    public CommonResponse<String> enforceDonation(
            @RequestPart("imageLists") List<MultipartFile> imageLists,
            @Parameter(
                    description = "",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart("enforceDonation") DonationReq.EnforceDonation enforceDonation){
        adminDonationService.enforceDonation(imageLists, enforceDonation);
        return CommonResponse.onSuccess("성공");
    }

    @GetMapping("/execution")
    @Operation(summary = "기부금 전반 내용 확인")
    public CommonResponse<PageResponse<List<DonationRes.ProjectDonationStatus>>> getProjectDonationStatus(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size
    ){
        return CommonResponse.onSuccess(adminDonationService.getProjectDonationStatus(page, size));
    }

    @GetMapping("/execution/{projectId}")
    @Operation(summary = "기부금 리스트 확인")
    public CommonResponse<PageResponse<List<DonationRes.ProjectDonationDto>>> getProjectDonationLists(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size,
            @PathVariable("projectId") Long projectId
    ){
        Project project = projectService.findByProjectId(projectId);
        return CommonResponse.onSuccess(adminDonationService.getProjectDonationLists(project, page, size));
    }

}
