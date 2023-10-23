package com.example.matchdomain.donation.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.EXECUTION_REFUND;
import static com.example.matchdomain.donation.exception.DonationListErrorCode.FILTER_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.DONATION_NOT_EXIST;
import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.REPRESENT;

@Adaptor
@RequiredArgsConstructor
public class DonationAdaptor {
    private final DonationUserRepository donationUserRepository;

    public Page<DonationUser> findDonationList(Long userId, int filter, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationUser> donationUsers = null;
        if(filter == 0){
            donationUsers = donationUserRepository.findByUserIdAndStatusAndDonationStatusNotOrderByCreatedAtDesc(userId, ACTIVE,EXECUTION_REFUND, pageable);
        }
        else if(filter == 1){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId, DonationStatus.EXECUTION_SUCCESS, ACTIVE, pageable);
        }
        else if(filter == 2){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId,DonationStatus.EXECUTION_SUCCESS, ACTIVE, pageable);

        }else if(filter == 3){
            donationUsers = donationUserRepository.findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(userId, DonationStatus.EXECUTION_SUCCESS, ACTIVE, pageable);

        }else{
            throw new BadRequestException(FILTER_NOT_EXIST);
        }

        return donationUsers;
    }

    public DonationUser findById(Long donationId){
        return donationUserRepository.findById(donationId).orElseThrow(() -> new NotFoundException(DONATION_NOT_EXIST));
    }

    public List<DonationUser> findByDonationCount(User user){
        return donationUserRepository.findByUserAndDonationStatusNotAndStatus(user, EXECUTION_REFUND, ACTIVE);
    }

    public Page<DonationUserRepository.flameList> findFlameList(User user, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        return donationUserRepository.getFlameList(user.getId(), pageable);
    }

    public List<DonationUser> findPayList(Long regularPayId){
        return donationUserRepository.findByRegularPaymentIdAndStatusOrderByCreatedAtDesc(regularPayId, ACTIVE);
    }

    public Page<DonationUser> getFlameProjectList(User user,String content, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        return donationUserRepository.findByUserAndInherenceNameContainingAndProject_ProjectImg_RepresentStatusOrderByCreatedAtDesc(user, content, REPRESENT, pageable);
    }

    public Page<DonationUser> findDonationUsers(Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return donationUserRepository.findByProjectId(projectId, pageable);
    }

    public Page<DonationUser> findByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return donationUserRepository.findByUserAndDonationStatusNotOrderByCreatedAtDesc(user, EXECUTION_REFUND, pageable);
    }
}
