package com.example.matchapi.order.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.InternalServerException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.repository.*;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.TURN_ON;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.CARD_NOT_CORRECT_USER;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.CARD_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationGerErrorCode.DONATION_NOT_EXIST;
import static com.example.matchdomain.order.exception.RegistrationCardErrorCode.FAILED_ERROR_ENCRYPT;

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

    @Transactional
    public NicePaymentAuth authPayment(String tid, Long amount) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader, tid, new NicePayRequest(String.valueOf(amount)));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());
        return nicePaymentAuth;
    }

    @Transactional
    public String payForProject(OrderReq.OrderDetail orderDetail) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader, orderDetail.getTid(), new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        return null;
    }


    @Transactional
    public NicePaymentAuth cancelPayment(String tid, String orderId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.cancelPayment(orderHelper.getNicePaymentAuthorizationHeader(), tid, new NicePayCancelRequest("단순취소", orderId));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        return nicePaymentAuth;
    }

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
    public void registrationCard(User user, OrderReq.RegistrationCard registrationCard) {
        String encrypt = encrypt(orderConvertor.createPlainText(registrationCard), nicePayProperties.getSecret().substring(0, 32), nicePayProperties.getSecret().substring(0, 16));
        String orderId = BILL + createRandomOrderId();
        Long userId = user.getId();
        //빌키 발급
        NicePayBillkeyResponse nicePayBillkeyResponse = niceAuthFeignClient.registrationCard(
                orderHelper.getNicePaymentAuthorizationHeader(),
                new NicePayRegistrationCardRequest(encrypt, orderId, "A2"));

        //나이스 카드 확인용 OrderId, Bid 필요
        NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(), nicePayBillkeyResponse.getBid(), orderConvertor.niceBillOk(nicePayBillkeyResponse, orderId));

        //에러코드 핸들링
        orderHelper.checkBillResult(niceBillOkResponse.getResultCode(), niceBillOkResponse.getResultMsg(), niceBillOkResponse.getTid(), niceBillOkResponse.getOrderId());

        //결제 완료 후
        //Billing Key 카드 저장
        UserCard userCard = userCardRepository.save(orderConvertor.UserCard(userId, registrationCard, nicePayBillkeyResponse));

    }

    @Transactional
    public String encrypt(String plainText, String secretKey, String iv) {
        SecretKey secureKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Hex.encodeHexString(encrypted);
        } catch (Exception e) {
            throw new InternalServerException(FAILED_ERROR_ENCRYPT);
        }
    }

    @Transactional
    public String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public String createRandomOrderId() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + UUID.randomUUID().toString();

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
                                    result.getCardCode(),
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
        NiceBillExpireResponse niceBillExpireResponse = niceAuthFeignClient.billKeyExpire(orderHelper.getNicePaymentAuthorizationHeader(), userCard.get().getBid(), new NiceBillExpireRequest(DELETE + createRandomOrderId()));
        System.out.println(niceBillExpireResponse.getResultCode() + niceBillExpireResponse.getResultMsg());
        userCard.get().setStatus(Status.INACTIVE);
    }


    @Transactional
    public void regularDonation(User user, OrderReq.RegularDonation regularDonation, Long cardId, Long projectId) {

        Optional<UserCard> card = userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE);

        String orderId = REGULAR + createRandomOrderId();

        NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(), card.get().getBid(), orderConvertor.billCardOneTime(regularDonation.getAmount(),orderId));

        orderHelper.checkNicePaymentsResult(niceBillOkResponse.getResultCode(), niceBillOkResponse.getResultMsg());

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        RegularPayment regularPayment = regularPaymentRepository.save(orderConvertor.RegularPayment(user.getId(), regularDonation, cardId, projectId));

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillUser(niceBillOkResponse, user.getId(), regularDonation.getAmount(), projectId, flameName, inherenceNumber, RegularStatus.REGULAR, regularPayment.getId()));

        donationHistoryRepository.save(donationConvertor.DonationHistoryTurnOn(regularPayment.getId(), TURN_ON));
        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));
    }

    @Transactional
    public void oneTimeDonationCard(User user, OrderReq.@Valid OneTimeDonation oneTimeDonation, Long cardId, Long projectId) {
        Optional<UserCard> card = userCardRepository.findByIdAndStatus(cardId, Status.ACTIVE);

        String orderId = ONE_TIME + createRandomOrderId();

        NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(), card.get().getBid(), orderConvertor.billCardOneTime(oneTimeDonation.getAmount(),orderId));

        orderHelper.checkNicePaymentsResult(niceBillOkResponse.getResultCode(), niceBillOkResponse.getResultMsg());

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        DonationUser donationUser = donationUserRepository.save(orderConvertor.donationBillUser(niceBillOkResponse, user.getId(), oneTimeDonation.getAmount(), projectId, flameName, inherenceNumber, RegularStatus.ONE_TIME, null));

        donationHistoryRepository.save(donationConvertor.DonationHistory(donationUser.getId(), CREATE));

    }

    @Transactional
    public String saveRequest(User user, Long projectId) {
        String orderId = ONE_TIME + createRandomOrderId();

        orderRequestRepository.save(orderConvertor.CreateRequest(user.getId(), projectId, orderId));

        return orderId;
    }

    @Transactional
    public String saveRequest(Long projectId) {
        String orderId = ONE_TIME + createRandomOrderId();

        orderRequestRepository.save(orderConvertor.CreateRequest(1L, projectId, orderId));

        return orderId;
    }

    @Transactional
    public OrderRequest getOrderRequest(String orderId) {
        Optional<OrderRequest> orderRequest = orderRequestRepository.findById(orderId);
        return orderRequest.get();

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
        cancelPayment(donationUser.getTid(), donationUser.getOrderId());
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
}