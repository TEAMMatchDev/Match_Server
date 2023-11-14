package com.example.matchapi.order.controller;

import com.example.matchapi.common.aop.CheckIdExist;
import com.example.matchapi.common.aop.CheckOneTimeProject;
import com.example.matchapi.common.aop.CheckRegularProject;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.order.dto.OrderCommand;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.order.mapper.OrderMapper;
import com.example.matchapi.order.service.CardService;
import com.example.matchapi.order.service.OrderService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.constants.MatchStatic;
import com.example.matchcommon.exception.errorcode.NicePayErrorCode;
import com.example.matchcommon.exception.errorcode.OtherServerErrorCode;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.donation.exception.DeleteCardErrorCode;
import com.example.matchdomain.order.exception.RegistrationCardErrorCode;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.exception.ProjectOneTimeErrorCode;
import com.example.matchdomain.project.exception.ProjectRegualrErrorCode;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.matchdomain.donation.entity.enums.RegularStatus.ONE_TIME;
import static com.example.matchdomain.donation.entity.enums.RegularStatus.REGULAR;


@RequestMapping("/order")
@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "04-Order💸",description = "NicePayment 결제 API")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CardService cardService;
    private final ProjectService projectService;
    private final OrderHelper orderHelper;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @PostMapping("/{projectId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 요청용 처음 결제할 때 요청 보내기 Web Version",description = "결제 요청용 API 입니다")
    @CheckIdExist
    public CommonResponse<String> requestPay(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "프로젝트 ID", example = "1") @PathVariable("projectId") Long projectId){
        log.info("결제 요청");
        String orderId = orderService.saveRequest(user, projectId);
        return CommonResponse.onSuccess(orderId);
    }

    @PostMapping("/v2/{projectId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 요청용 처음 결제할 때 요청 보내기 V2 flutter 인 경우 여기로 요청 보내주세요",description = "결제 요청용 API 입니다")
    @CheckIdExist
    public CommonResponse<String> requestPayPrepare(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "프로젝트 ID", example = "1") @PathVariable("projectId") Long projectId,
            @RequestParam int amount
    ){
        log.info("결제 준비 요청 v2");
        String orderId = orderService.saveRequestPrepare(user, projectId, amount);
        return CommonResponse.onSuccess(orderId);
    }

    @PostMapping("/pay/card")
    @ApiErrorCodeExample({UserAuthErrorCode.class, OtherServerErrorCode.class, RegistrationCardErrorCode.class})
    @Operation(summary = "04-02 Order💸 정기 결제용 카드 등록 api",description = "정기 결제를 위한 카드 등록 API 입니다.")
    public CommonResponse<PortOneBillResponse> registrationCard(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderReq.RegistrationCard registrationCard){
        return CommonResponse.onSuccess(orderService.postCard(user, registrationCard));
    }

    @GetMapping("/pay/card")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "04-03 Order💸 정기 결제용 카드 조회 api #FRAME 결제 화면 - 단기,정기 결제", description = "정기 결제를 위한 카드 조회 API 입니다..")
    public CommonResponse<List<OrderRes.UserBillCard>> getUserBillCard(@Parameter(hidden = true) @AuthenticationPrincipal User user){
        return CommonResponse.onSuccess(orderService.getUserBillCard(user.getId()));
    }

    @DeleteMapping("/pay/card/{cardId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, DeleteCardErrorCode.class})
    @Operation(summary = "04-04 Order💸 정기,단기 결제용 카드 삭제 api #FRAME 결제 화면 - 단기,정기 결제", description = "정기 결제를 위한 카드 삭제 API 입니다..")
    @CheckIdExist
    public CommonResponse<String> deleteBillCard(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                                                      @Parameter(description = "카드 ID", example = "1") @PathVariable("cardId") Long cardId){
        orderService.deleteBillCard(cardId);
        return CommonResponse.onSuccess("삭제 성공");
    }

    @PostMapping("/pay/card/{cardId}/{projectId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, OtherServerErrorCode.class, ProjectRegualrErrorCode.class, DeleteCardErrorCode.class})
    @Operation(summary = "04-05 Order💸 정기 결제 등록 api #FRAME 결제 화면 - 정기 결제",description = "정기 결제 신청하기 API 입니다.")
    public CommonResponse<OrderRes.CompleteDonation> regularDonation(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "카드 id",example = "1") @PathVariable Long cardId,
            @Parameter(description = "프로젝트 id", example = "2") @PathVariable Long projectId,
            @Valid @RequestBody OrderReq.RegularDonation regularDonation){
        UserCard card = cardService.findByCardId(cardId);
        Project project = projectService.checkProjectExists(projectId, REGULAR);
        String orderId = orderHelper.createOrderId(MatchStatic.REGULAR);
        System.out.println(orderId);
        return CommonResponse.onSuccess(orderService.paymentForRegular(orderMapper.toRegularDonation(card, regularDonation, user, project, orderId)));
    }

    @PostMapping("/pay/one/card/{cardId}/{projectId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, OtherServerErrorCode.class, ProjectOneTimeErrorCode.class, DeleteCardErrorCode.class})
    @Operation(summary = "04-06 Order💸 빌키로 단기 결제 api #FRAME 결제 화면 - 단기 결제",description = "단 결제 신청하기 API 입니다.")
    public CommonResponse<OrderRes.CompleteDonation> oneTimeDonationCard(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "카드 id",example = "1") @PathVariable Long cardId,
            @Parameter(description = "프로젝트 id", example = "2") @PathVariable Long projectId,
            @Valid @RequestBody OrderReq.OneTimeDonation oneTimeDonation){
        UserCard card = cardService.findByCardId(cardId);
        Project project = projectService.checkProjectExists(projectId, ONE_TIME);
        String orderId = orderHelper.createOrderId(MatchStatic.ONE_TIME);
        log.info(orderId);
        return CommonResponse.onSuccess(orderService.paymentForOnetime(orderMapper.toOneTimeDonation(card, oneTimeDonation, user, project, orderId)));
    }

    @PostMapping("/user")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "04-07 Order💸 후원자 정보조회",description = "후원자 정보조회 API 입니다.")
    public CommonResponse<OrderRes.UserDetail> getUserInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal User user
            ){
        return CommonResponse.onSuccess(userService.getUserInfo(user));
    }


/*    @DeleteMapping("/revoke/{cardId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "04-08 Order💸 간편결제 해지",description = "간편결제 해지 입니다.")
    public CommonResponse<String> revokePay(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @PathVariable Long cardId
    ) {
        orderService.revokePay(user, cardId);
        return CommonResponse.onSuccess("간편 결제 해지");
    }*/
}
