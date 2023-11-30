package com.example.matchapi.order.service;

import com.example.matchapi.common.security.JwtService;
import com.example.matchapi.donation.service.DonationHistoryService;
import com.example.matchapi.order.converter.OrderConverter;
import com.example.matchapi.order.dto.OrderCommand;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.service.PaymentService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchapi.user.service.AligoService;
import com.example.matchcommon.annotation.PaymentIntercept;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.BaseException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
import com.example.matchdomain.donation.adaptor.RequestFailedHistoryAdapter;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.CardAbleStatus;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.repository.*;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.adaptor.UserCardAdaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.aligo.converter.AligoConverter;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.pay.portone.converter.PortOneConverter;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOnePrepareReq;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.service.PortOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.*;
import static com.example.matchdomain.donation.exception.DonationGerErrorCode.DONATION_NOT_EXIST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {
    private final DonationUserRepository donationUserRepository;
    private final OrderConverter orderConverter;
    private final OrderHelper orderHelper;
    private final RegularPaymentRepository regularPaymentRepository;
    private final UserCardRepository userCardRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConverter portOneConverter;
    private final PaymentService paymentService;
    private final RegularPaymentAdaptor regularPaymentAdaptor;
    private final UserCardAdaptor userCardAdaptor;
    private final DonationHistoryService donationHistoryService;
    private final RequestFailedHistoryAdapter failedHistoryAdapter;
    private final PortOneService portOneService;
    private final AligoService aligoService;
    private final AligoConverter aligoConverter;
    private final JwtService jwtService;

    @Transactional
    public List<OrderRes.UserBillCard> getUserBillCard(Long userId) {
        List<UserCard> userCards = userCardAdaptor.findCardLists(userId);

        return orderConverter.convertToUserCardLists(userCards);
    }

    @RedissonLock(LockName = "카드-삭제", key = "#cardId")
    public void deleteBillCard(Long cardId) {
        UserCard userCard = userCardAdaptor.findCardByCardId(cardId);

        List<RegularPayment> regularPayments = regularPaymentAdaptor.findByCardId(cardId);

        cancelRegularPayment(regularPayments);

        String accessToken = portOneAuthService.getToken();

        portOneFeignClient.deleteBillKey(accessToken, userCard.getBid());

        userCard.setStatus(Status.INACTIVE);

        userCardRepository.save(userCard);
    }

    private void validateCard(UserCard card, User user) {
        if (!card.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);

        if (!card.getCardAbleStatus().equals(CardAbleStatus.ABLE)) throw new BadRequestException(CARD_NOT_ABLE);
    }

    @PaymentIntercept(key = "#orderCommand.orderId")
    @RedissonLock(LockName = "빌키-단기-기부", key = "#orderCommand.userCard.id")
    public OrderRes.CompleteDonation paymentForOnetime(OrderCommand.OneTimeDonation orderCommand) {
        UserCard card = orderCommand.getUserCard();
        Project project = orderCommand.getProject();
        User user = orderCommand.getUser();
        OrderReq.OneTimeDonation oneTimeDonation = orderCommand.getOneTimeDonation();

        System.out.println(orderCommand.getOrderId());

        PortOneResponse<PortOneBillPayResponse> portOneResponse = paymentService.payBillKey(card, oneTimeDonation.getAmount(), project.getProjectName(), orderCommand.getOrderId());

        OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(user);

        DonationUser donationUser = donationUserRepository.save(
                orderConverter.donationBillPayUser(portOneResponse.getResponse(), user.getId(),
                        oneTimeDonation.getAmount(), project.getId(),
                        createInherenceDto, RegularStatus.ONE_TIME, null));

        donationHistoryService.oneTimeDonationHistory(donationUser.getId());

        aligoService.sendAlimTalk(jwtService.createToken(1L), AlimType.PAYMENT, aligoConverter.convertToAlimTalkPayment(donationUser.getId(), user.getName(), user.getPhoneNumber()));

        return orderConverter.convertToCompleteDonation(user.getName(), project, oneTimeDonation.getAmount());
    }

    @PaymentIntercept(key = "#orderCommand.orderId")
    @RedissonLock(LockName = "정기-기부-신청", key = "#orderCommand.userCard.id")
    public OrderRes.CompleteDonation paymentForRegular(OrderCommand.RegularDonation orderCommand) {
        UserCard card = orderCommand.getUserCard();
        Project project = orderCommand.getProject();
        User user = orderCommand.getUser();

        OrderReq.RegularDonation regularDonation = orderCommand.getRegularDonation();

        PortOneResponse<PortOneBillPayResponse> portOneResponse = paymentService.payBillKey(card, regularDonation.getAmount(), project.getProjectName(), orderCommand.getOrderId());

        OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(user);

        RegularPayment regularPayment = regularPaymentRepository.save(orderConverter.convertToRegularPayment(user.getId(), regularDonation, card.getId(), project.getId()));

        DonationUser donationUser = donationUserRepository.save(orderConverter.donationBillPayUser(
                portOneResponse.getResponse(), user.getId(), regularDonation.getAmount(), project.getId(),
                createInherenceDto, RegularStatus.REGULAR, regularPayment.getId()));

        donationHistoryService.postRegularDonationHistory(regularPayment.getId(), donationUser.getId());

        aligoService.sendAlimTalk(jwtService.createToken(1L), AlimType.PAYMENT, aligoConverter.convertToAlimTalkPayment(donationUser.getId(), user.getName(), user.getPhoneNumber()));

        return orderConverter.convertToCompleteDonation(user.getName(), project, regularDonation.getAmount());
    }

    @Transactional
    public String saveRequest(User user, Long projectId) {
        String orderId = orderHelper.createOrderId(ONE_TIME);

        orderRequestRepository.save(orderConverter.CreateRequest(user.getId(), projectId, orderId));

        PortOnePrepareReq portOnePrepareReq = portOneConverter.convertToRequestPrepare(orderId, 1000);

        portOneFeignClient.preparePayments(portOneAuthService.getToken(), portOnePrepareReq);

        return orderId;
    }

    @RedissonLock(LockName = "관리자-환불-처리", key = "#donationUserId")
    public void adminRefundDonation(Long donationUserId) {
        DonationUser donationUser = donationUserRepository.findById(donationUserId).orElseThrow(() -> new BadRequestException(DONATION_NOT_EXIST));
        donationUser.setDonationStatus(DonationStatus.EXECUTION_REFUND);
        portOneService.refundPayment(donationUser.getTid());
        donationUserRepository.save(donationUser);
    }

    @Transactional
    public void modifyDonationStatus(Long donationUserId, DonationStatus donationStatus) {
        DonationUser donationUser = donationUserRepository.findById(donationUserId).orElseThrow(() -> new BadRequestException(DONATION_NOT_EXIST));
        donationUser.setDonationStatus(donationStatus);
        donationUserRepository.save(donationUser);
    }

    @Transactional
    public void revokePay(User user, Long cardId) {
        UserCard userCard = userCardAdaptor.findCardByCardId(cardId);
        if (!userCard.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);
        List<RegularPayment> regularPayments = regularPaymentRepository.findByUserCardId(cardId);

        for (RegularPayment regularPayment : regularPayments) {
            regularPayment.setRegularPayStatus(RegularPayStatus.USER_CANCEL);
        }

        regularPaymentRepository.saveAll(regularPayments);
    }

    @RedissonLock(LockName = "유저-카드-등록", key = "#user.id")
    public PortOneBillResponse postCard(User user, OrderReq.RegistrationCard registrationCard) {
        String accessToken = portOneAuthService.getToken();
        String cardNo = orderHelper.formatString(registrationCard.getCardNo(), 4);
        String expiry = "20" + registrationCard.getExpYear() + "-" + registrationCard.getExpMonth();
        PortOneResponse<PortOneBillResponse> portOneResponse = portOneFeignClient.getBillKey(
                accessToken,
                orderHelper.createOrderId(BILL),
                portOneConverter.convertToPortOneBill(cardNo, expiry, registrationCard.getIdNo(), registrationCard.getCardPw())
        );

        if (portOneResponse.getCode() != 0) {
            throw new BaseException(BAD_REQUEST, false, "PORT_ONE_BILL_AUTH_001", portOneResponse.getMessage());
        }

        System.out.println(portOneResponse.getResponse().getCard_code());
        userCardRepository.save(orderConverter.convertToUserBillCard(user.getId(), registrationCard, portOneResponse.getResponse()));

        return portOneResponse.getResponse();
    }


    private void cancelRegularPayment(List<RegularPayment> regularPayments) {
        for (RegularPayment regularPayment : regularPayments) {
            regularPayment.setRegularPayStatus(RegularPayStatus.USER_CANCEL);
            failedHistoryAdapter.deleteByRegularPaymentId(regularPayment.getId());
        }
        regularPaymentRepository.saveAll(regularPayments);
    }

    public String saveRequestPrepare(User user, Long projectId, int amount) {
        String orderId = orderHelper.createOrderId(ONE_TIME);

        orderRequestRepository.save(orderConverter.convertToRequestPrepare(user.getId(), projectId, amount, orderId));

        PortOnePrepareReq portOnePrepareReq = portOneConverter.convertToRequestPrepare(orderId, amount);

        portOneFeignClient.preparePayments(portOneAuthService.getToken(), portOnePrepareReq);

        return orderId;
    }
}
