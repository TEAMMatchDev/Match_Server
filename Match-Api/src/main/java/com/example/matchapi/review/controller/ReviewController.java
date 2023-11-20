package com.example.matchapi.review.controller;

import com.example.matchapi.review.dto.ReviewRes;
import com.example.matchapi.review.service.ReviewService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.donation.exception.CheckExecutionCode;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "13-Review 관련 API")
public class ReviewController {
    private final ReviewService reviewService;
    @ApiErrorCodeExample({UserAuthErrorCode.class, CheckExecutionCode.class})
    @GetMapping("")
    @Operation(summary = "13-01 Review 작성 유무 팝업 창 띄우기 유무",description = "매 번 스플레시 단 or 홈화면에서 호출 하여 팝업창 띄우기")
    public CommonResponse<ReviewRes.PopUpInfo> checkPopUp(@AuthenticationPrincipal User user){

        return CommonResponse.onSuccess(reviewService.checkPopUp(user));
    }

}
