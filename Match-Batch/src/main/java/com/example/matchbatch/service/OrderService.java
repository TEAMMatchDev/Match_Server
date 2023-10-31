package com.example.matchbatch.service;

import com.example.matchbatch.OrderHelper;
import com.example.matchbatch.convertor.DonationConvertor;
import com.example.matchbatch.convertor.OrderConvertor;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.repository.DonationHistoryRepository;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.donation.repository.RequestPaymentHistoryRepository;
import com.example.matchinfrastructure.discord.client.DiscordFeignClient;
import com.example.matchinfrastructure.discord.convertor.DiscordConvertor;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkResponse;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.matchcommon.constants.MatchStatic.REGULAR;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;
import static com.example.matchdomain.donation.entity.enums.PaymentStatus.COMPLETE;
import static com.example.matchdomain.donation.entity.enums.PaymentStatus.FAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final RegularPaymentRepository regularPaymentRepository;
    private final OrderHelper orderHelper;
    private final OrderConvertor orderConvertor;
    private final RequestPaymentHistoryRepository regularPaymentHistoryRepository;
    private final DonationUserRepository donationUserRepository;
    private final DiscordFeignClient discordFeignClient;
    private final DiscordConvertor discordConvertor;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationConvertor donationConvertor;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConvertor portOneConvertor;

    @Transactional
    public void regularDonationPayment() {

        List<RegularPayment> regularPayments = calculateDay();
        List<RequestPaymentHistory> requestPaymentHistories = new ArrayList<>();
        List<DonationUser> donationUsers = new ArrayList<>();
        List<DonationHistory> donationHistories = new ArrayList<>();

        int trueCnt = 0;
        int amount = 0;
        int successCnt = 0;

        discordFeignClient.alertMessage(discordConvertor.convertToAlertBatchMessage("정기 결제 스케줄러 시작", regularPayments.size()));

        String accessToken = portOneAuthService.getTokens();

        if (regularPayments.size() > 0) {
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonthValue();
            for (RegularPayment regularPayment : regularPayments) {
                if(regularPayment.getCreatedAt().getMonthValue()==currentMonth & regularPayment.getCreatedAt().getYear()==currentYear) {
                    log.info("not pay Day of Month " + "userId :" + regularPayment.getUserId()+ " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount() + "원 projectId :" + regularPayment.getProjectId() + " payId : "+ regularPayment.getId());
                }
                else{
                    trueCnt +=1 ;
                    UserCard userCard = regularPayment.getUserCard();
                    Long userId = regularPayment.getUserId();

                    String orderId = REGULAR + createRandomOrderId();

                    PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(accessToken, portOneConvertor.convertPayWithBillKey(userCard.getBid(), orderId, regularPayment.getAmount(), LocalDateTime.now().getDayOfMonth() + "월 달 " +"MATCH 기부금 정기 결제", userCard.getCustomerId()));


                    String flameName = orderHelper.createFlameName(regularPayment.getUser().getName());

                    String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

                    DonationUser donationUser = donationUserRepository.save(orderConvertor.donationUser(portOneResponse.getResponse(), userId, flameName, inherenceNumber, regularPayment.getProjectId(),regularPayment.getId()));
                    donationHistories.add(donationConvertor.DonationHistory(donationUser.getId(), CREATE ));

                    //requestPaymentHistories.add(orderConvertor.RegularHistory(niceBillOkResponse, userId, COMPLETE, "SUCCESS",regularPayment.getId(), regularPayment.getPayDate(),regularPayment.getUserCardId()));
                    log.info("success Payment " + "userId :" + regularPayment.getUserId() + " orderId : " + orderId + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount() + "원 projectId :" + regularPayment.getProjectId());
                    amount += regularPayment.getAmount();
                    successCnt += 1;

                }
            }
            donationHistoryRepository.saveAll(donationHistories);
            regularPaymentHistoryRepository.saveAll(requestPaymentHistories);
            discordFeignClient.alertMessage(discordConvertor.convertToAlertFinishMessage("정기 결제 스케줄러 종료", amount, regularPayments.size(),successCnt, trueCnt));

        }
    }

    @Transactional
    public List<RegularPayment> calculateDay() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 이번달에 마지막 날짜
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 오늘 날짜 구하기
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDay == lastDayOfMonth) {
            //현재 날짜와 달의 마지막 날짜가 같거나 클때의 로직
            System.out.println("현재 날짜가 같아요");
            return regularPaymentRepository.findByPayDateGreaterThanEqualAndStatusAndRegularPayStatus(currentDay, Status.ACTIVE, RegularPayStatus.PROCEEDING);
        } else {
            // 현재 날짜가 같지 않을 때의 로직
            System.out.println("현재 날짜가 달라요");
            return regularPaymentRepository.findByPayDateGreaterThanEqualAndStatusAndRegularPayStatus(currentDay, Status.ACTIVE, RegularPayStatus.PROCEEDING);
        }
    }

    public String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public String createRandomOrderId() {
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(12, useLetters, useNumbers);

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + randomStr;
    }

    @Transactional
    public void regularPaymentRetry() {
        List<DonationUser> donationUsers = new ArrayList<>();

        List<RequestPaymentHistory> requestPaymentHistories = regularPaymentHistoryRepository.findByPaymentStatusAndStatus(FAIL, Status.ACTIVE);

        List<DonationHistory> donationHistories = new ArrayList<>();

        int amount = 0;
        int trueCnt = 0;
        int successCnt = 0 ;
        String accessToken = portOneAuthService.getTokens();
        discordFeignClient.alertMessage(discordConvertor.convertToAlertBatchMessage("정기 결제 실패 한 리스트 스케줄러가 시작",  requestPaymentHistories.size()));

        for(RequestPaymentHistory requestPaymentHistory : requestPaymentHistories){
            RegularPayment regularPayment = requestPaymentHistory.getRegularPayment();
            UserCard userCard = requestPaymentHistory.getUserCard();
            String orderId = REGULAR + createRandomOrderId();

            PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(accessToken, portOneConvertor.convertPayWithBillKey(userCard.getBid(), orderId, regularPayment.getAmount(), LocalDateTime.now().getDayOfMonth() + "월 달 " +"MATCH 기부금 정기 결제", userCard.getCustomerId()));

            trueCnt +=1 ;

            String flameName = orderHelper.createFlameName(requestPaymentHistory.getUser().getName());

            String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

            donationUsers.add(orderConvertor.donationUser(portOneResponse.getResponse(), requestPaymentHistory.getUserId(), flameName, inherenceNumber, regularPayment.getProjectId(), regularPayment.getId()));


            requestPaymentHistory.setPaymentStatus(COMPLETE);

            amount += regularPayment.getAmount();
            successCnt +=1;

            log.info("success Payment Retry historyId: " + requestPaymentHistory.getId()+" userId :" + regularPayment.getUserId() + " orderId : " + orderId + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount() + "원 projectId :" + regularPayment.getProjectId());

        }

        donationUserRepository.saveAll(donationUsers);

        discordFeignClient.alertMessage(discordConvertor.convertToAlertFinishMessage("실패한 정기 결제 스케줄러가 종료", amount, requestPaymentHistories.size(),successCnt,trueCnt));

    }

}
