package com.example.matchapi.donation.service;

import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.donation.entity.DonationStatus.EXECUTION_BEFORE;
import static com.example.matchdomain.donation.entity.DonationStatus.EXECUTION_REFUND;
import static com.example.matchdomain.donation.exception.DonationListErrorCode.FILTER_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.*;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationUserRepository donationUserRepository;
    private final OrderService orderService;

    public PageResponse<List<DonationRes.DonationList>> getDonationList(Long userId, int filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationUser> donationUsers = null;

        List<DonationRes.DonationList> donationLists = new ArrayList<>();

        if(filter == 0){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusNot(userId, EXECUTION_REFUND, pageable);
        }
        else if(filter == 1){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatus(userId, DonationStatus.EXECUTION_BEFORE, pageable);
        }
        else if(filter == 2){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatus(userId, DonationStatus.EXECUTION_UNDER, pageable);

        }else if(filter == 3){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatus(userId, DonationStatus.EXECUTION_SUCCESS, pageable);

        }else{
            throw new BadRequestException(FILTER_NOT_EXIST);
        }

        donationUsers.getContent().forEach(
                result ->{
                    donationLists.add(
                            new DonationRes.DonationList(
                                    result.getId(),
                                    result.getDonationStatus().getName(),
                                    result.getInherenceName(),
                                    result.getRegularStatus().getName()
                            )
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
}
