package com.example.matchbatch.service;

import com.example.matchbatch.OrderHelper;
import com.example.matchbatch.convertor.OrderConvertor;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.entity.RequestPaymentHistory;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.donation.repository.RequestPaymentHistoryRepository;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.matchcommon.constants.MatchStatic.REGULAR;
import static com.example.matchdomain.donation.entity.PaymentStatus.COMPLETE;
import static com.example.matchdomain.donation.entity.PaymentStatus.FAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final RegularPaymentRepository regularPaymentRepository;
    private final OrderHelper orderHelper;
    private final OrderConvertor orderConvertor;
    private final RequestPaymentHistoryRepository regularPaymentHistoryRepository;
    private final DonationUserRepository donationUserRepository;

    @Transactional
    public void regularDonationPayment() {

        List<RegularPayment> regularPayments = calculateDay();
        List<RequestPaymentHistory> requestPaymentHistories = new ArrayList<>();
        List<DonationUser> donationUsers = new ArrayList<>();

        if (regularPayments.size() > 0) {
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonthValue();
            for (RegularPayment regularPayment : regularPayments) {
                if(regularPayment.getCreatedAt().getMonthValue()==currentMonth & regularPayment.getCreatedAt().getYear()==currentYear) {
                    log.info("not pay Day of Month " + "userId :" + regularPayment.getUserId()+ " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount() + "원 projectId :" + regularPayment.getProjectId() + " payId : "+ regularPayment.getId());
                }
                else{
                    Long userId = regularPayment.getUserId();
                    NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(),
                            regularPayment.getUserCard().getBid(),
                            orderConvertor.niceBillRequest(regularPayment, REGULAR + createRandomOrderId()));
                    if (niceBillOkResponse.getResultCode().equals("0000")) {
                        String flameName = orderHelper.createFlameName(regularPayment.getUser().getName());

                        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

                        donationUsers.add(orderConvertor.donationUser(niceBillOkResponse, userId, flameName, inherenceNumber, regularPayment.getProjectId(),regularPayment.getId()));

                        requestPaymentHistories.add(orderConvertor.RegularHistory(niceBillOkResponse, userId, COMPLETE, "SUCCESS",regularPayment.getId(), regularPayment.getPayDate(),regularPayment.getUserCardId()));
                        log.info("success Payment " + "userId :" + regularPayment.getUserId() + " orderId : " + niceBillOkResponse.getOrderId() + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount() + "원 projectId :" + regularPayment.getProjectId());
                    } else {
                        requestPaymentHistories.add(orderConvertor.RegularHistory(niceBillOkResponse, userId, FAIL, niceBillOkResponse.getResultMsg(), regularPayment.getId(), regularPayment.getPayDate(), regularPayment.getUserCardId()));

                        log.info("fail Payment " + "userId :" + regularPayment.getUserId() + " orderId : " + niceBillOkResponse.getOrderId() + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount());
                    }
                }
            }
            donationUserRepository.saveAll(donationUsers);
            regularPaymentHistoryRepository.saveAll(requestPaymentHistories);
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
            return regularPaymentRepository.findByPayDateGreaterThanEqualAndStatus(currentDay, Status.ACTIVE);
        } else {
            // 현재 날짜가 같지 않을 때의 로직
            System.out.println("현재 날짜가 달라요");
            return regularPaymentRepository.findByPayDateAndStatus(currentDay, Status.ACTIVE);
        }
    }

    public String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public String createRandomOrderId() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + UUID.randomUUID().toString();

    }

    @Transactional
    public void regularPaymentRetry() {
        List<DonationUser> donationUsers = new ArrayList<>();

        List<RequestPaymentHistory> requestPaymentHistories = regularPaymentHistoryRepository.findByPaymentStatusAndStatus(FAIL, Status.ACTIVE);

        for(RequestPaymentHistory requestPaymentHistory : requestPaymentHistories){
            RegularPayment regularPayment = requestPaymentHistory.getRegularPayment();
            UserCard userCard = requestPaymentHistory.getUserCard();
            NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(),
                    userCard.getBid(),
                    orderConvertor.niceBillRequest(requestPaymentHistory.getRegularPayment(), REGULAR + createRandomOrderId()));

            if (niceBillOkResponse.getResultCode().equals("0000")) {
                String flameName = orderHelper.createFlameName(requestPaymentHistory.getUser().getName());

                String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

                donationUsers.add(orderConvertor.donationUser(niceBillOkResponse, requestPaymentHistory.getUserId(), flameName, inherenceNumber, regularPayment.getProjectId(), regularPayment.getId()));

                requestPaymentHistory.setPaymentStatus(COMPLETE);

                log.info("success Payment Retry historyId: " + requestPaymentHistory.getId()+" userId :" + regularPayment.getUserId() + " orderId : " + niceBillOkResponse.getOrderId() + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount() + "원 projectId :" + regularPayment.getProjectId());
            } else {
                log.info("fail Payment Retry historyId: " + requestPaymentHistory.getId()+ " userId :" + regularPayment.getUserId() + " orderId : " + niceBillOkResponse.getOrderId() + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount());
            }
        }

        donationUserRepository.saveAll(donationUsers);

    }

}
