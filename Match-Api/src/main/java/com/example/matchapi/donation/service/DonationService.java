package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.order.service.OrderService;
import com.example.matchapi.project.convertor.ProjectConvertor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.repository.DonationHistoryRepository;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchcommon.constants.MatchStatic.FIRST_TIME;
import static com.example.matchcommon.constants.MatchStatic.LAST_TIME;
import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.donation.entity.DonationStatus.*;
import static com.example.matchdomain.donation.entity.HistoryStatus.CREATE;
import static com.example.matchdomain.donation.entity.RegularPayStatus.USER_CANCEL;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_CORRECT_USER;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationListErrorCode.FILTER_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.*;
import static com.example.matchdomain.donation.exception.GetRegularErrorCode.REGULAR_NOT_EXIST;
import static com.example.matchdomain.project.entity.ImageRepresentStatus.REPRESENT;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationUserRepository donationUserRepository;
    private final OrderService orderService;
    private final DonationConvertor donationConvertor;
    private final RegularPaymentRepository regularPaymentRepository;
    private final DonationHelper donationHelper;
    private final ProjectConvertor projectConvertor;
    private final DonationHistoryRepository donationHistoryRepository;

    public PageResponse<List<DonationRes.DonationList>> getDonationList(Long userId, int filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationUser> donationUsers = null;

        List<DonationRes.DonationList> donationLists = new ArrayList<>();

        if(filter == 0){
            donationUsers = donationUserRepository.findByUserIdAndStatusAndDonationStatusNotOrderByCreatedAtDesc(userId, ACTIVE,EXECUTION_REFUND, pageable);
        }
        else if(filter == 1){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, ACTIVE, pageable);
        }
        else if(filter == 2){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, ACTIVE, pageable);

        }else if(filter == 3){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, ACTIVE, pageable);

        }else{
            throw new BadRequestException(FILTER_NOT_EXIST);
        }

        donationUsers.getContent().forEach(
                result ->{
                    donationLists.add(
                            donationConvertor.DonationList(result)
                    );
                }
        );



        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), donationLists);
    }

    @Transactional
    public void refundDonation(User user, Long donationId) {
        DonationUser donationUser = donationUserRepository.findById(donationId).orElseThrow(() -> new NotFoundException(DONATION_NOT_EXIST));
        if(!donationUser.getUserId().equals(user.getId())) throw new BadRequestException(DONATION_NOT_CORRECT_USER);
        if(!donationUser.getDonationStatus().equals(EXECUTION_BEFORE)) throw new BadRequestException(CANNOT_DELETE_DONATION_STATUS);
        orderService.cancelPayment(donationUser.getTid(), donationUser.getOrderId());
        donationUser.setDonationStatus(EXECUTION_REFUND);
    }

    //불꽃이 필터링 0 = 불꽃이 전체, 1 = 전달 전 불꽃이, 2 = 절달 중인 불꽃이, 3 = 전달 완료된 불꽃이
    //정렬 필터링 0 = 최신순, 1 = 오래된 순, 2 = 기부금액 큰 순, 3 = 기부금액 작은 순
    public PageResponse<List<DonationRes.FlameList>> getFlameList(User user, int page, int size, int flame, int order, String content) {
        Pageable pageable = PageRequest.of(page, size);

        Page<DonationUser> donationUsers = null;

        List<DonationRes.FlameList> flameLists = new ArrayList<>();

        if(flame == 0){
            if(content == null){
                if(order == 0 ){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByCreatedAtDesc(user.getId(), EXECUTION_REFUND, ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByCreatedAtAsc(user.getId(),EXECUTION_REFUND, ACTIVE, pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByPriceDesc(user.getId(), EXECUTION_REFUND, ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByPriceAsc(user.getId(), EXECUTION_REFUND, ACTIVE, pageable);
                }
            }
            else{
                if(order == 0 ){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtDesc(user.getId(), EXECUTION_REFUND, content, content, content, ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtAsc(user.getId(), EXECUTION_REFUND, content, content, content, ACTIVE, pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceDesc(user.getId(), EXECUTION_REFUND, content, content, content, ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceAsc(user.getId(), EXECUTION_REFUND, content, content, content, ACTIVE, pageable);
                }
            }
        }
        else {
            DonationStatus donationStatus = null;
            if(flame == 1){
                donationStatus = EXECUTION_BEFORE;
            }
            else if(flame ==2){
                donationStatus = EXECUTION_UNDER;
            }
            else{
                donationStatus = EXECUTION_SUCCESS;
            }

            if(content == null){
                if(order == 0 ){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(user.getId(), donationStatus, ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtAsc(user.getId(),donationStatus, ACTIVE,pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByPriceDesc(user.getId(), donationStatus, ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByPriceAsc(user.getId(), donationStatus, ACTIVE, pageable);
                }
            }
            else{
                if(order == 0 ){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtDesc(user.getId(), donationStatus, content, content, content, ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtAsc(user.getId(), donationStatus, content, content, content, ACTIVE, pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceDesc(user.getId(), donationStatus, content, content, content, ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceAsc(user.getId(), donationStatus, content, content, content, ACTIVE, pageable);
                }
            }
        }

        donationUsers.getContent().forEach(
                result -> flameLists.add(
                        donationConvertor.Flame(result)
                )
        );


        return new PageResponse<>(donationUsers.isLast(),donationUsers.getTotalElements(),flameLists);
    }

    public void cancelRegularPay(User user, Long regularId) {
        RegularPayment regularPayment = regularPaymentRepository.findByIdAndStatus(regularId, ACTIVE).orElseThrow(() -> new BadRequestException(REGULAR_PAY_NOT_EXIST));

        if(!regularPayment.getUserId().equals(user.getId())) throw new BadRequestException(REGULAR_PAY_NOT_CORRECT_USER);

        regularPayment.setRegularPayStatus(USER_CANCEL);
        regularPaymentRepository.save(regularPayment);
    }

    public DonationRes.DonationCount getDonationCount(User user) {
        List<DonationUser> donationUser = donationUserRepository.findByUserAndDonationStatusNotAndStatus(user, EXECUTION_REFUND, ACTIVE);

        return donationConvertor.DonationCount(donationUser);
    }

    @Transactional
    public DonationRes.DonationInfo getDonationInfo() {
        LocalDate localDate = LocalDate.now();

        List<DonationUser> donationUsers = donationUserRepository.findByDonationStatusNot(EXECUTION_REFUND);

        int oneDayDonationAmount = 0;
        int weekendDonationAmount = 0;
        int monthlyDonationAmount = 0;
        for (DonationUser donationUser : donationUsers) {
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate+FIRST_TIME))&&donationUser.getCreatedAt().isBefore(LocalDateTime.parse(localDate+LAST_TIME))){
                oneDayDonationAmount += donationUser.getPrice();
            }
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME))&&donationUser.getCreatedAt().isBefore(LocalDateTime.parse(localDate+LAST_TIME))){
                weekendDonationAmount += donationUser.getPrice();
            }
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME))&&donationUser.getCreatedAt().isBefore( LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME))){
                monthlyDonationAmount += donationUser.getPrice();
            }
        }

        return new DonationRes.DonationInfo(donationHelper.parsePriceComma(oneDayDonationAmount),donationHelper.parsePriceComma(weekendDonationAmount),donationHelper.parsePriceComma(monthlyDonationAmount));



    }

    public DonationRes.DonationDetail getDonationDetail(Long donationId) {
        DonationUser donationUser = donationUserRepository.findById(donationId).orElseThrow(()-> new BadRequestException(DONATION_NOT_EXIST));
        return donationConvertor.getDonationDetail(donationUser);
    }

    public PageResponse<List<DonationRes.BurningMatchRes>> getBurningMatch(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationUserRepository.flameList> flameLists = donationUserRepository.getFlameList(user.getId(), pageable);

        List<DonationRes.BurningMatchRes> burningMatchRes = new ArrayList<>();

        flameLists.forEach(
                result -> {
                    burningMatchRes.add(donationConvertor.BurningMatch(result));
                }
        );


        return new PageResponse<>(flameLists.isLast(), flameLists.getTotalElements(), burningMatchRes);
    }

    @Transactional
    public DonationRes.DonationRegular getDonationRegular(Long regularPayId, User user) {
        RegularPayment regularPayment = regularPaymentRepository.findById(regularPayId).orElseThrow(()-> new BadRequestException(REGULAR_NOT_EXIST));
        return donationConvertor.DonationRegular(regularPayment);
    }

    @Transactional
    public PageResponse<List<DonationRes.DonationRegularList>> getDonationRegularList(Long regularPayId, User user, int page, int size) {
        System.out.println("존재 유무 확인");
        RegularPayment regularPayment = regularPaymentRepository.findById(regularPayId).orElseThrow(()-> new BadRequestException(REGULAR_NOT_EXIST));
        Pageable pageable = PageRequest.of(page, size);
        System.out.println("페이지 네이션");
        Page<DonationHistory> donationHistories = donationHistoryRepository.findByRegularPaymentIdOrderByCreatedAtDesc(regularPayId,pageable);

        List<DonationRes.DonationRegularList> donationRegularLists = new ArrayList<>();

        donationHistories.forEach(
                result -> donationRegularLists.add(
                        donationConvertor.DonationRegularList(result)
                )
        );

        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationRegularLists);
    }

    public List<DonationRes.PayList> getPayList(User user, Long regularPayId) {
        List<DonationUser> donationUsers = donationUserRepository.findByRegularPaymentIdAndStatusOrderByCreatedAtDesc(regularPayId, ACTIVE);
        List<DonationRes.PayList> payLists = new ArrayList<>();

        donationUsers.forEach(
                result -> payLists.add(
                        donationConvertor.PayList(result)
                )
        );

        return payLists;
    }

    public DonationRes.FlameProject getFlameProjectList(User user, String content) {
        List<DonationUser> donationUsers = donationUserRepository.findByUserAndInherenceNameContainingAndProject_ProjectImg_RepresentStatusOrderByCreatedAtDesc(user, content, REPRESENT);
        List<DonationRes.FlameProjectList> flameProjectLists = new ArrayList<>();

        System.out.println(donationUsers.size());
        donationUsers.forEach(
                result -> flameProjectLists.add(
                        donationConvertor.FlameProject(result)
                )
        );
        return new DonationRes.FlameProject(donationUsers.size(), flameProjectLists);
    }

    public PageResponse<List<DonationRes.DonationRegularList>> getFlameRegularList(Long donationId, User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        DonationUser donationUser = donationUserRepository.findById(donationId).orElseThrow(()-> new BadRequestException(DONATION_NOT_EXIST));
        List<DonationRes.DonationRegularList> donationRegularLists = new ArrayList<>();
        Page<DonationHistory> donationHistories = donationHistoryRepository.getDonationHistoryCustom(donationUser.getRegularPaymentId(), donationId, CREATE, pageable);
        donationHistories.forEach(
                result -> donationRegularLists.add(
                        donationConvertor.DonationRegularList(result)
                )
        );

        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationRegularLists);
    }

    public DonationRes.DonationFlame getFlameRegular(Long donationId, User user) {
        DonationUser donationUser = donationUserRepository.findById(donationId).orElseThrow(()-> new BadRequestException(DONATION_NOT_EXIST));
        RegularPayment regularPayment = regularPaymentRepository.findById(donationUser.getRegularPaymentId()).orElseThrow(()-> new BadRequestException(REGULAR_NOT_EXIST));
        return donationConvertor.DonationFlame(regularPayment, donationUser);
    }
}
