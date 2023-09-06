package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.donation.entity.DonationStatus.*;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_CORRECT_USER;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationListErrorCode.FILTER_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.*;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationUserRepository donationUserRepository;
    private final OrderService orderService;
    private final DonationConvertor donationConvertor;
    private final RegularPaymentRepository regularPaymentRepository;

    public PageResponse<List<DonationRes.DonationList>> getDonationList(Long userId, int filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationUser> donationUsers = null;

        List<DonationRes.DonationList> donationLists = new ArrayList<>();

        if(filter == 0){
            donationUsers = donationUserRepository.findByUserIdAndStatusAndDonationStatusNotOrderByCreatedAtDesc(userId, Status.ACTIVE,EXECUTION_REFUND, pageable);
        }
        else if(filter == 1){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, Status.ACTIVE, pageable);
        }
        else if(filter == 2){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, Status.ACTIVE, pageable);

        }else if(filter == 3){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, Status.ACTIVE, pageable);

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
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByCreatedAtDesc(user.getId(), EXECUTION_REFUND, Status.ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByCreatedAtAsc(user.getId(),EXECUTION_REFUND, Status.ACTIVE, pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByPriceDesc(user.getId(), EXECUTION_REFUND, Status.ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotAndStatusOrderByPriceAsc(user.getId(), EXECUTION_REFUND, Status.ACTIVE, pageable);
                }
            }
            else{
                if(order == 0 ){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtDesc(user.getId(), EXECUTION_REFUND, content, content, content, Status.ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtAsc(user.getId(), EXECUTION_REFUND, content, content, content, Status.ACTIVE, pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceDesc(user.getId(), EXECUTION_REFUND, content, content, content, Status.ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceAsc(user.getId(), EXECUTION_REFUND, content, content, content, Status.ACTIVE, pageable);
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
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(user.getId(), donationStatus, Status.ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtAsc(user.getId(),donationStatus, Status.ACTIVE,pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByPriceDesc(user.getId(), donationStatus, Status.ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByPriceAsc(user.getId(), donationStatus, Status.ACTIVE, pageable);
                }
            }
            else{
                if(order == 0 ){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtDesc(user.getId(), donationStatus, content, content, content, Status.ACTIVE, pageable);
                }
                else if(order == 1){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtAsc(user.getId(), donationStatus, content, content, content, Status.ACTIVE, pageable);
                }
                else if(order == 2){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceDesc(user.getId(), donationStatus, content, content, content, Status.ACTIVE, pageable);
                }
                else if(order ==3){
                    donationUsers = donationUserRepository.findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceAsc(user.getId(), donationStatus, content, content, content, Status.ACTIVE, pageable);
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
        RegularPayment regularPayment = regularPaymentRepository.findByIdAndStatus(regularId, Status.ACTIVE).orElseThrow(() -> new BadRequestException(REGULAR_PAY_NOT_EXIST));

        if(!regularPayment.getUserId().equals(user.getId())) throw new BadRequestException(REGULAR_PAY_NOT_CORRECT_USER);

        regularPayment.setStatus(Status.INACTIVE);
        regularPaymentRepository.save(regularPayment);
    }

    public DonationRes.DonationCount getDonationCount(User user) {
        List<DonationUser> donationUser = donationUserRepository.findByUserAndDonationStatusNotAndStatus(user, EXECUTION_REFUND, Status.ACTIVE);

        return donationConvertor.DonationCount(donationUser);
    }
}
