package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationTemporaryConvertor;
import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donationTemporary.entity.*;
import com.example.matchdomain.donationTemporary.repository.DonationListRepository;
import com.example.matchdomain.donationTemporary.repository.DonationTemporaryRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.donationTemporary.exception.AdminDonationRequestErrorCode.NOT_EXIST_DONATION_REQUEST;

@Service
@RequiredArgsConstructor
public class DonationTemporaryService {
   private final DonationTemporaryConvertor donationTemporaryConvertor;
   private final DonationTemporaryRepository donationTemporaryRepository;
   private final DonationListRepository donationListRepository;
   private final DonationHelper donationHelper;
    public DonationTemporaryRes.UserInfo getUserInfo(User user) {
        return donationTemporaryConvertor.UserInfo(user);
    }

    public void postDonationTemporary(User user, DonationTemporaryReq.DonationInfo donationInfo) {
        if(donationInfo.getAlarmMethod() == AlarmMethod.SMS) {
            donationTemporaryRepository.save(donationTemporaryConvertor.DonationInfo(user, donationInfo));
        }else{
            donationTemporaryRepository.save(donationTemporaryConvertor.DonationInfoEmail(user, donationInfo));
        }
    }

    public PageResponse<List<DonationTemporaryRes.DonationList>> getDonationList(DonationKind donationKind, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationList> donationLists = donationListRepository.findByDonationTemporary_DonationKindAndDonationTemporary_DepositOrderByCreatedAtDesc(donationKind, Deposit.EXISTENCE, pageable);
        List<DonationTemporaryRes.DonationList> donationList = new ArrayList<>();

        donationLists.getContent().forEach(
                result -> donationList.add(
                        donationTemporaryConvertor.DonationList(result, donationHelper.parsePriceComma(result.getAmount()))
                )
        );

        return new PageResponse<>(donationLists.isLast(), donationLists.getTotalElements(), donationList);
    }

    public PageResponse<List<DonationTemporaryRes.DonationRequestAdminList>> getDonationRequestList(Deposit deposit, int page, int size, String content) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationTemporary> donationTemporaries = null;
        if(content ==null) {
            if (deposit.equals(Deposit.ALL)) {
                donationTemporaries = donationTemporaryRepository.findByStatusOrderByCreatedAtDesc(Status.ACTIVE,pageable);
            } else {
                donationTemporaries = donationTemporaryRepository.findByDepositOrderByCreatedAtDesc(deposit, pageable);
            }
        }
        else{
            if (deposit.equals(Deposit.ALL)) {
                donationTemporaries = donationTemporaryRepository.findByNameContainingOrderByCreatedAtDesc(content,pageable);
            } else {
                donationTemporaries = donationTemporaryRepository.findByNameContainingAndDepositOrderByCreatedAtDesc(content,deposit, pageable);
            }
        }
        return new PageResponse<>(donationTemporaries.isLast(), donationTemporaries.getTotalElements(), donationTemporaryConvertor.DonationRequestAdminList(donationTemporaries.getContent()));
    }

    @Transactional
    public void postDonationDeposit(DonationTemporaryReq.DonationDeposit donationDeposit) {
        DonationTemporary donationTemporary = donationTemporaryRepository.findById(donationDeposit.getDonationRequestId())
                .orElseThrow(()-> new BadRequestException(NOT_EXIST_DONATION_REQUEST));

        donationListRepository.save(donationTemporaryConvertor.DonationDeposit(donationDeposit));

        donationTemporary.setDeposit(Deposit.EXISTENCE);
        donationTemporaryRepository.save(donationTemporary);
    }

    public DonationTemporaryRes.DonationDetail getDonationInfo(Long donationRequestId) {
        DonationTemporary donationTemporary = donationTemporaryRepository.findById(donationRequestId)
                .orElseThrow(()-> new BadRequestException(NOT_EXIST_DONATION_REQUEST));
        return donationTemporaryConvertor.DonationInfoDetail(donationTemporary);
    }
}
