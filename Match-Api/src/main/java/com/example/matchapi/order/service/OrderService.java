package com.example.matchapi.order.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.service.PaymentService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.CardAbleStatus;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.repository.*;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.TURN_ON;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.*;
import static com.example.matchdomain.donation.exception.DonationGerErrorCode.DONATION_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final OrderHelper orderHelper;
    private final RegularPaymentRepository regularPaymentRepository;
    private final UserCardRepository userCardRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationConvertor donationConvertor;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConvertor portOneConvertor;
    private final PaymentService paymentService;
    private final ProjectService projectService;
    private final DonationAdaptor donationAdaptor;
    private final RegularPaymentAdaptor regularPaymentAdaptor;

    @Transactional
    public List<OrderRes.UserBillCard> getUserBillCard(Long userId) {
        List<UserCard> userCards = userCardRepository.findByUserIdAndStatus(userId,Status.ACTIVE);
        List<OrderRes.UserBillCard> userBillCards = new ArrayList<>();

        userCards.forEach(
                result -> {
                    userBillCards.add(
                            new OrderRes.UserBillCard(
                                    result.getId(),
                                    result.getCardCode().getName(),
                                    result.getCardName(),
                                    orderHelper.maskMiddleNum(result.getCardNo()),
                                    result.getCardAbleStatus().getName()
                            )
                    );
                }
        );
        return userBillCards;
    }

    @RedissonLock(LockName = "카드-삭제", key = "#cardId")
    public void deleteBillCard(Long cardId) {
        UserCard userCard = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));

        List<RegularPayment> regularPayments = regularPaymentAdaptor.findByCardId(cardId);

        for(RegularPayment regularPayment : regularPayments){
            regularPayment.setRegularPayStatus(RegularPayStatus.USER_CANCEL);
        }

        regularPaymentRepository.saveAll(regularPayments);

        portOneFeignClient.deleteBillKey(portOneAuthService.getToken(), userCard.getBid());

        userCard.setStatus(Status.INACTIVE);

        userCardRepository.save(userCard);
    }


    @RedissonLock(LockName = "정기-기부-신청", key = "#cardId")
    public void regularDonation(User user, OrderReq.RegularDonation regularDonation, Long cardId, Long projectId) {
        UserCard card = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));

        if(!card.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);

        if(!card.getCardAbleStatus().equals(CardAbleStatus.ABLE)) throw new BadRequestException(CARD_NOT_ABLE);

        Project project = projectService.checkProjectExists(projectId, RegularStatus.REGULAR);

        PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(portOneAuthService.getToken(), portOneConvertor.PayWithBillKey(card.getBid(), createOrderId(REGULAR), regularDonation.getAmount(), project.getProjectName(), card.getCustomerId()));

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = createRandomUUID();

        RegularPayment regularPayment = regularPaymentRepository.save(orderConvertor.RegularPayment(user.getId(), regularDonation, cardId, projectId));

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillPayUser(portOneResponse.getResponse(), user.getId(), regularDonation.getAmount(), projectId, flameName, inherenceNumber, RegularStatus.REGULAR, regularPayment.getId()));

        donationHistoryRepository.save(donationConvertor.DonationHistoryTurnOn(regularPayment.getId(), TURN_ON));

        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));
    }

    @RedissonLock(LockName = "빌키-단기-기부", key = "#cardId")
    public void oneTimeDonationCard(User user, OrderReq.OneTimeDonation oneTimeDonation, Long cardId, Long projectId) {
        UserCard card = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));

        Project project = projectService.checkProjectExists(projectId, RegularStatus.ONE_TIME);

        PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(portOneAuthService.getToken(), portOneConvertor.PayWithBillKey(card.getBid(), createOrderId(ONE_TIME), oneTimeDonation.getAmount(), project.getProjectName(), card.getCustomerId()));

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = createRandomUUID();

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillPayUser(portOneResponse.getResponse(), user.getId(), oneTimeDonation.getAmount(), projectId, flameName, inherenceNumber, RegularStatus.ONE_TIME, null));

        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));
    }

    @Transactional
    public String saveRequest(User user, Long projectId) {
        String orderId = createOrderId(ONE_TIME);

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
        UserCard userCard = userCardRepository.findByIdAndStatus(cardId, Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));
        if(!userCard.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);
        List<RegularPayment> regularPayments = regularPaymentRepository.findByUserCardId(cardId);

        for(RegularPayment regularPayment : regularPayments){
            regularPayment.setRegularPayStatus(RegularPayStatus.USER_CANCEL);
        }

        regularPaymentRepository.saveAll(regularPayments);
    }

    @RedissonLock(LockName = "유저-카드-등록", key = "user")
    public PortOneBillResponse postCard(User user, OrderReq.RegistrationCard registrationCard) {
        String accessToken = portOneAuthService.getToken();
        String cardNo = formatString(registrationCard.getCardNo(), 4);
        String expiry = "20" + registrationCard.getExpYear() + "-" + registrationCard.getExpMonth();
        PortOneResponse<PortOneBillResponse> portOneResponse = portOneFeignClient.getBillKey(
                accessToken,
                createOrderId(BILL),
                portOneConvertor.PortOneBill(cardNo, expiry, registrationCard.getIdNo(), registrationCard.getCardPw())
        );
        if(portOneResponse.getCode()!=0){
            throw new BaseException(HttpStatus.BAD_REQUEST, false, "PORT_ONE_BILL_AUTH_001", portOneResponse.getMessage());
        }
        userCardRepository.save(orderConvertor.UserBillCard(user.getId(), registrationCard, portOneResponse.getResponse()));

        return portOneResponse.getResponse();
    }

    public String formatString(String input, int length) {
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i > 0 && i % length == 0) {
                formatted.append('-');
            }
            formatted.append(input.charAt(i));
        }

        return formatted.toString();
    }

    public String createRandomUUID() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + UUID.randomUUID().toString();
    }

    public String createOrderId(String type){
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(12, useLetters, useNumbers);
        return type + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + randomStr;
    }
}