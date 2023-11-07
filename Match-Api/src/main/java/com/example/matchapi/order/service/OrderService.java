package com.example.matchapi.order.service;

import com.example.matchapi.donation.service.DonationHistoryService;
import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.service.PaymentService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.BaseException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
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
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOnePrepareReq;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.*;
import static com.example.matchdomain.donation.exception.DonationGerErrorCode.DONATION_NOT_EXIST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final OrderHelper orderHelper;
    private final RegularPaymentRepository regularPaymentRepository;
    private final UserCardRepository userCardRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConvertor portOneConvertor;
    private final PaymentService paymentService;
    private final ProjectService projectService;
    private final RegularPaymentAdaptor regularPaymentAdaptor;
    private final UserCardAdaptor userCardAdaptor;
    private final DonationHistoryService donationHistoryService;

    @Transactional
    public List<OrderRes.UserBillCard> getUserBillCard(Long userId) {
        List<UserCard> userCards = userCardAdaptor.findCardLists(userId);

        return orderConvertor.convertToUserCardLists(userCards);
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

    @RedissonLock(LockName = "정기-기부-신청", key = "#cardId")
    public OrderRes.CompleteDonation regularDonation(User user, OrderReq.RegularDonation regularDonation, Long cardId, Long projectId) {
        UserCard card = userCardAdaptor.findCardByCardId(cardId);

        validateCard(card, user);

        Project project = projectService.checkProjectExists(projectId, RegularStatus.REGULAR);

        PortOneResponse<PortOneBillPayResponse> portOneResponse = paymentService.payBillKey(card, regularDonation.getAmount(), project.getProjectName(), REGULAR);

        OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(user);

        RegularPayment regularPayment = regularPaymentRepository.save(orderConvertor.convertToRegularPayment(user.getId(), regularDonation, cardId, projectId));

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillPayUser(portOneResponse.getResponse(), user.getId(), regularDonation.getAmount(), projectId, createInherenceDto, RegularStatus.REGULAR, regularPayment.getId()));

        donationHistoryService.postRegularDonationHistory(regularPayment.getId(), donationUser.getId());

        return orderConvertor.convertToCompleteDonation(user.getName(), project, regularDonation.getAmount());
    }


    private void validateCard(UserCard card, User user) {
        if(!card.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);

        if(!card.getCardAbleStatus().equals(CardAbleStatus.ABLE)) throw new BadRequestException(CARD_NOT_ABLE);
    }

    @RedissonLock(LockName = "빌키-단기-기부", key = "#cardId")
    public OrderRes.CompleteDonation oneTimeDonationCard(User user, OrderReq.OneTimeDonation oneTimeDonation, Long cardId, Long projectId) {
        try {
            UserCard card = userCardAdaptor.findCardByCardId(cardId);

            validateCard(card, user);

            Project project = projectService.checkProjectExists(projectId, RegularStatus.ONE_TIME);

            PortOneResponse<PortOneBillPayResponse> portOneResponse = paymentService.payBillKey(card, oneTimeDonation.getAmount(), project.getProjectName(), ONE_TIME);

            OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(user);

            DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillPayUser(portOneResponse.getResponse(), user.getId(), oneTimeDonation.getAmount(), projectId,
                    createInherenceDto, RegularStatus.ONE_TIME, null));

            donationHistoryService.oneTimeDonationHistory(donationUser.getId());

            return orderConvertor.convertToCompleteDonation(user.getName(), project, oneTimeDonation.getAmount());
        }catch (Exception e){
            //paymentService.refundPayment();
        }

        return null;
    }

    @Transactional
    public String saveRequest(User user, Long projectId) {
        String orderId = orderHelper.createOrderId(ONE_TIME);

        orderRequestRepository.save(orderConvertor.CreateRequest(user.getId(), projectId, orderId));

        return orderId;
    }

    @RedissonLock(LockName = "관리자-환불-처리", key = "#donationUserId")
    public void adminRefundDonation(Long donationUserId) {
        DonationUser donationUser = donationUserRepository.findById(donationUserId).orElseThrow(()-> new BadRequestException(DONATION_NOT_EXIST));
        donationUser.setDonationStatus(DonationStatus.EXECUTION_REFUND);
        paymentService.refundPayment(donationUser.getTid());
        donationUserRepository.save(donationUser);
    }

    @Transactional
    public void modifyDonationStatus(Long donationUserId, DonationStatus donationStatus) {
        DonationUser donationUser = donationUserRepository.findById(donationUserId).orElseThrow(()-> new BadRequestException(DONATION_NOT_EXIST));
        donationUser.setDonationStatus(donationStatus);
        donationUserRepository.save(donationUser);
    }

    @Transactional
    public void revokePay(User user, Long cardId) {
        UserCard userCard = userCardAdaptor.findCardByCardId(cardId);
        if(!userCard.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);
        List<RegularPayment> regularPayments = regularPaymentRepository.findByUserCardId(cardId);

        for(RegularPayment regularPayment : regularPayments){
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
                portOneConvertor.convertToPortOneBill(cardNo, expiry, registrationCard.getIdNo(), registrationCard.getCardPw())
        );

        if(portOneResponse.getCode()!=0){
            throw new BaseException(BAD_REQUEST, false, "PORT_ONE_BILL_AUTH_001", portOneResponse.getMessage());
        }

        System.out.println(portOneResponse.getResponse().getCard_code());
        userCardRepository.save(orderConvertor.convertToUserBillCard(user.getId(), registrationCard, portOneResponse.getResponse()));

        return portOneResponse.getResponse();
    }



    private void cancelRegularPayment(List<RegularPayment> regularPayments) {
        for(RegularPayment regularPayment : regularPayments){
            regularPayment.setRegularPayStatus(RegularPayStatus.USER_CANCEL);
        }
        regularPaymentRepository.saveAll(regularPayments);
    }

    public String saveRequestPrepare(User user, Long projectId, int amount) {
        String orderId = orderHelper.createOrderId(ONE_TIME);

        orderRequestRepository.save(orderConvertor.convertToRequestPrepare(user.getId(), projectId, amount, orderId));

        PortOnePrepareReq portOnePrepareReq = portOneConvertor.convertToRequestPrepare(orderId, amount);

        portOneFeignClient.preparePayments(portOneAuthService.getToken(), portOnePrepareReq);

        return orderId;
    }
}