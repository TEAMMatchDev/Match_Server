package com.example.matchapi.donation.controller;

import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.exception.*;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Tag(name = "05-Donation💸",description = "기부금 관련 API 입니다.")
public class DonationController {
    private final DonationService donationService;
    private final ProjectService projectService;

    @PatchMapping("/{donationId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, DonationRefundErrorCode.class})
    @Operation(summary = "05-02 Donation💸 가부금 환불")
    public CommonResponse<String> refundDonation(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "기부금 id") @PathVariable Long donationId
    ) {
        donationService.refundDonation(user, donationId);
        return CommonResponse.onSuccess("기부금 환불 성공");
    }

    /*
    @GetMapping("/flame/filter")
    @Deprecated
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "05-03 Donation💸 홈화면 불꽃이 필터링 조회")
    public CommonResponse<PageResponse<List<DonationRes.FlameList>>> getFlameList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size,
            @Parameter(description = "불꽃이 필터링 0 = 불꽃이 전체, 1 = 전달 전 불꽃이, 2 = 전달 중인 불꽃이, 3 = 전달 완료된 불꽃이", example = "0") @RequestParam(required = false, defaultValue = "0") int flame,
            @Parameter(description = "정렬 필터링 0 = 최신순, 1 = 오래된 순, 2 = 기부금액 큰 순, 3 = 기부금액 작은 순", example = "0") @RequestParam(required = false, defaultValue = "0") int order,
            @Parameter(description = "검색어") @RequestParam(required = false) String content
    ) {
        return CommonResponse.onSuccess(donationService.getFlameList(user, page, size, flame, order, content));
    }

     */

    @DeleteMapping("/{regularId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, CancelRegularPayErrorCode.class})
    @Operation(summary = "05-04 Donation💸 정기 결제 해지 API")
    public CommonResponse<String> cancelRegularPay(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "정기결제 id") @PathVariable Long regularId) {
        donationService.cancelRegularPay(user, regularId);
        return CommonResponse.onSuccess("해지 성공");
    }

    @GetMapping("/status")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "05-05-01 Donation💸 정기 결제 상태 상단 조회")
    public CommonResponse<DonationRes.DonationCount> getDonationCount(
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        return CommonResponse.onSuccess(donationService.getDonationCount(user));
    }


    @GetMapping("")
    @ApiErrorCodeExample({DonationListErrorCode.class, UserAuthErrorCode.class})
    @Operation(summary = "05-05-02 Donation💸 기부 리스트 조회")
    public CommonResponse<PageResponse<List<DonationRes.DonationList>>> getDonationList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size,
            @Parameter(description = "필터 전체 조회 0 집행 전 1 집행 중 2 집행완료 3") @RequestParam(required = false, defaultValue = "0") int filter) {
        return CommonResponse.onSuccess(donationService.getDonationList(user.getId(), filter, page, size));
    }

/*    @GetMapping("/burning-match")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "05-06 유저의 불타는 매치 #FRAME_홈_불타는 매치")
    public CommonResponse<PageResponse<List<DonationRes.BurningMatchRes>>> getBurningMatch(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size
    ) {
        return CommonResponse.onSuccess(donationService.getBurningMatch(user, page, size));
    }*/

  /*  @GetMapping("/top/{regularPayId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, GetRegularErrorCode.class})
    @Operation(summary = "05-07-01 후원 상세 보기 조회 #FRAME_불타는 매치_후원_상세_보기_상단조회")
    public CommonResponse<DonationRes.DonationRegular> getDonationRegular(@PathVariable Long regularPayId,
                                                                          @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return CommonResponse.onSuccess(donationService.getDonationRegular(regularPayId, user));
    }


    @GetMapping("/bottom/{regularPayId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, GetRegularErrorCode.class})
    @Operation(summary = "05-07-02 후원 상세 보기 조회 #FRAME_불타는 매치_후원_상세_보기_하단조회")
    public CommonResponse<PageResponse<List<DonationRes.DonationRegularList>>> getDonationRegularList(
            @PathVariable Long regularPayId,
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size
    ) {
        return CommonResponse.onSuccess(donationService.getDonationRegularList(regularPayId, user, page, size));
    }*/

    @GetMapping("/pay/{regularPayId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, GetRegularErrorCode.class})
    @Operation(summary = "05-08 매치 결제 내역 리스트 조회 #FRAME_불타는 매치_매치 결제 내역")
    public CommonResponse<List<DonationRes.PayList>> getPayList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @PathVariable Long regularPayId) {
        return CommonResponse.onSuccess(donationService.getPayList(user, regularPayId));
    }

    @GetMapping("/flame")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "05-09 매치 불꽃이 고유 이름 검색 #FRAME_홈")
    public CommonResponse<PageResponse<List<DonationRes.FlameProjectList>>> getFlameProjectList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "검색어") @RequestParam(required = false) String content,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size
    ) {
        return CommonResponse.onSuccess(donationService.getFlameProjectList(user, content, page, size));
    }

    @GetMapping("/flame/top/{donationId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, GetRegularErrorCode.class, DonationGerErrorCode.class})
    @Operation(summary = "05-10-01 불꽃이 상세 보기 조회 #FRAME_불타는 매치_후원_상세_보기_상단조회")
    public CommonResponse<DonationRes.DonationFlame> getFlameRegular(@PathVariable Long donationId,
                                                                       @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return CommonResponse.onSuccess(donationService.getFlameRegular(donationId, user));
    }


    @GetMapping("/flame/bottom/{donationId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, GetRegularErrorCode.class, DonationGerErrorCode.class})
    @Operation(summary = "05-10-02 불꽃이 상세 보기 조회 #FRAME_불타는 매치_후원_상세_보기_하단조회")
    public CommonResponse<PageResponse<List<DonationRes.DonationRegularList>>> getFlameRegularList(
            @PathVariable Long donationId,
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "5") int size
    ) {
        return CommonResponse.onSuccess(donationService.getFlameRegularList(donationId, user, page, size));
    }

    @Operation(summary = "05-11 진행 중인 매치 조회 #FRAME_기부 내역",description = "진행중인 매치 조회 API 입니다.")
    @GetMapping("/match")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    public CommonResponse<PageResponse<List<DonationRes.MatchList>>> getUserMatchList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        PageResponse<List<DonationRes.MatchList>> matchLists = donationService.getUserMatchList(user, page, size);
        return CommonResponse.onSuccess(matchLists);
    }

    @Operation(summary = "05-12 타오르는 불꽃이 리스트 조회 #FRAME_불꽃이_둘러보기", description = "타오르는 불꽃이 리스트 조회입니다, 후원중인 곳이 없을 때 빈 리스트가 반환됩니다.")
    @GetMapping("/burning-flame")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    public CommonResponse<PageResponse<List<DonationRes.BurningFlameDto>>> getBurningFlameList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        return CommonResponse.onSuccess(donationService.getBurningFlameList(user, page, size));
    }


    @Operation(summary = "05-13 튜토리얼 기부 리스트", description = "튜토리얼 기부 리스트")
    @GetMapping("/tutorial")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    public CommonResponse<List<DonationRes.Tutorial>> getTutorialDonation(
        @AuthenticationPrincipal User user
    ){
        return CommonResponse.onSuccess(projectService.getTutorialDonation());
    }

    @Operation(summary = "05-14 튜토리얼 기부 ", description = "튜토리얼 1원 기부 POST API 입니다.")
    @PostMapping("/tutorial")
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class})
    @RedissonLock(LockName = "유저 튜토리얼 기부", key = "#user.id")
    public CommonResponse<DonationRes.CompleteDonation> postTutorialDonation(
            @AuthenticationPrincipal User user,
            @RequestBody DonationReq.Tutorial tutorial){
        Project project = projectService.findByProjectId(tutorial.getProjectId());
        return CommonResponse.onSuccess(donationService.postTutorialDonation(user, tutorial, project));
    }
}