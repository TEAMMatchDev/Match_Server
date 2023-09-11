package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationTemporaryConvertor;
import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import com.example.matchdomain.donationTemporary.repository.DonationTemporaryRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationTemporaryService {
   private final DonationTemporaryConvertor donationTemporaryConvertor;
   private final DonationTemporaryRepository donationTemporaryRepository;
    public DonationTemporaryRes.UserInfo getUserInfo(User user) {
        return donationTemporaryConvertor.UserInfo(user);
    }

    public void postDonationTemporary(User user, DonationTemporaryReq.DonationInfo donationInfo) {
        donationTemporaryRepository.save(donationTemporaryConvertor.DonationInfo(user, donationInfo));
    }
}
