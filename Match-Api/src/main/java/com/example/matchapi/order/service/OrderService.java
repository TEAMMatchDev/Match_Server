package com.example.matchapi.order.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.service.PaymentService;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.exception.InternalServerException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.CardAbleStatus;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.repository.*;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.*;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.TURN_ON;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.*;
import static com.example.matchdomain.donation.exception.DonationGerErrorCode.DONATION_NOT_EXIST;
import static com.example.matchdomain.order.exception.RegistrationCardErrorCode.FAILED_ERROR_ENCRYPT;
import static com.example.matchdomain.project.exception.ProjectOneTimeErrorCode.PROJECT_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final NicePayProperties nicePayProperties;
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final OrderHelper orderHelper;
    private final RegularPaymentRepository regularPaymentRepository;
    private final UserCardRepository userCardRepository;
    private final RequestPaymentHistoryRepository requestPaymentHistoryRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final UserRepository userRepository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationConvertor donationConvertor;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConvertor portOneConvertor;
    private final ProjectRepository projectRepository;
    private final PaymentService paymentService;

    @Transactional
    public String requestPayment(User user, OrderReq.OrderDetail orderDetail, Long projectId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.
                paymentAuth(orderHelper.getNicePaymentAuthorizationHeader(),
                        orderDetail.getTid(),
                        new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        donationUserRepository.save(orderConvertor.donationUser(nicePaymentAuth, user.getId(), orderDetail, projectId, flameName, inherenceNumber));

        return flameName;
    }

    @Transactional
    public String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public String createRandomId(){
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(12, useLetters, useNumbers);

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + randomStr;
    }
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

    @Transactional
    public void deleteBillCard(Long cardId) {
        Optional<UserCard> userCard = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE);
        NiceBillExpireResponse niceBillExpireResponse = niceAuthFeignClient.billKeyExpire(orderHelper.getNicePaymentAuthorizationHeader(), userCard.get().getBid(), new NiceBillExpireRequest(DELETE + createRandomId()));
        System.out.println(niceBillExpireResponse.getResultCode() + niceBillExpireResponse.getResultMsg());
        userCard.get().setStatus(Status.INACTIVE);
    }


    @Transactional
    public void regularDonation(User user, OrderReq.RegularDonation regularDonation, Long cardId, Long projectId) {

        UserCard card = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));
        if(!card.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);
        if(!card.getCardAbleStatus().equals(CardAbleStatus.ABLE)) throw new BadRequestException(CARD_NOT_ABLE);
        Project project = projectRepository.findByIdAndStatusAndRegularStatus(projectId, Status.ACTIVE, RegularStatus.REGULAR).orElseThrow(() ->new BadRequestException(PROJECT_NOT_EXIST));

        String orderId = REGULAR + createRandomId();

        String accessToken = portOneAuthService.getToken();

        PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(accessToken, portOneConvertor.PayWithBillKey(card.getBid(), orderId, regularDonation.getAmount(), project.getProjectName(), card.getCustomerId()));

        System.out.println(portOneResponse.getCode());
        System.out.println(portOneResponse.getMessage());

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        RegularPayment regularPayment = regularPaymentRepository.save(orderConvertor.RegularPayment(user.getId(), regularDonation, cardId, projectId));
        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillPayUser(portOneResponse.getResponse(), user.getId(), regularDonation.getAmount(), projectId, flameName, inherenceNumber, RegularStatus.REGULAR, regularPayment.getId()));
        donationHistoryRepository.save(donationConvertor.DonationHistoryTurnOn(regularPayment.getId(), TURN_ON));
        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));
    }

    @Transactional
    public void oneTimeDonationCard(User user, OrderReq. @Valid OneTimeDonation oneTimeDonation, Long cardId, Long projectId) {
        UserCard card = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));

        Project project = projectRepository.findByIdAndStatusAndRegularStatus(projectId, Status.ACTIVE, RegularStatus.ONE_TIME).orElseThrow(() ->new BadRequestException(PROJECT_NOT_EXIST));

        String orderId = ONE_TIME + createRandomId();

        String accessToken = portOneAuthService.getToken();

        PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(accessToken, portOneConvertor.PayWithBillKey(card.getBid(), orderId, oneTimeDonation.getAmount(), project.getProjectName(), card.getCustomerId()));

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillPayUser(portOneResponse.getResponse(), user.getId(), oneTimeDonation.getAmount(), projectId, flameName, inherenceNumber, RegularStatus.ONE_TIME, null));

        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));

    }

    @Transactional
    public String saveRequest(User user, Long projectId) {
        String orderId = ONE_TIME + createRandomId();

        orderRequestRepository.save(orderConvertor.CreateRequest(user.getId(), projectId, orderId));

        return orderId;
    }

    @Transactional
    public void requestPaymentAuth(String tid, Long amount) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.
                paymentAuth(orderHelper.getNicePaymentAuthorizationHeader(),
                        tid,
                        new NicePayRequest(String.valueOf(amount)));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        Optional<OrderRequest> orderRequest = orderRequestRepository.findById(nicePaymentAuth.getOrderId());

        Optional<User> user = userRepository.findByIdAndStatus(Long.valueOf(orderRequest.get().getUserId()),Status.ACTIVE);

        String flameName = orderHelper.createFlameName(user.get().getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationUserV2(nicePaymentAuth, user.get().getId(), amount, orderRequest.get().getProjectId(), flameName, inherenceNumber));

        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));

    }

    @Transactional
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

    public final synchronized String getyyyyMMddHHmmss(){
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }

    public PortOneBillResponse postCard(User user, OrderReq.RegistrationCard registrationCard) {
        String accessToken = portOneAuthService.getToken();
        String customerUid = BILL + createRandomId();
        String cardNo = formatString(registrationCard.getCardNo(), 4);
        String expiry = "20" + registrationCard.getExpYear() + "-" + registrationCard.getExpMonth();
        PortOneResponse<PortOneBillResponse> portOneResponse = portOneFeignClient.getBillKey(
                accessToken,
                customerUid,
                portOneConvertor.PortOneBill(cardNo, expiry, registrationCard.getIdNo(), registrationCard.getCardPw())
        );
        if(portOneResponse.getCode()!=0){
            throw new BaseException(HttpStatus.BAD_REQUEST, false, "PORT_ONE_BILL_AUTH_001", portOneResponse.getMessage());
        }
        UserCard userCard = userCardRepository.save(orderConvertor.UserBillCard(user.getId(), registrationCard, portOneResponse.getResponse()));

        return portOneResponse.getResponse();
    }

    public static String formatString(String input, int length) {
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i > 0 && i % length == 0) {
                formatted.append('-');
            }
            formatted.append(input.charAt(i));
        }

        return formatted.toString();
    }
}