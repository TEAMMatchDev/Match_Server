package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationTemporaryConvertor;
import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donationTemporary.entity.Deposit;
import com.example.matchdomain.donationTemporary.entity.DonationKind;
import com.example.matchdomain.donationTemporary.entity.DonationList;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import com.example.matchdomain.donationTemporary.repository.DonationListRepository;
import com.example.matchdomain.donationTemporary.repository.DonationTemporaryRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        donationTemporaryRepository.save(donationTemporaryConvertor.DonationInfo(user, donationInfo));
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
}
