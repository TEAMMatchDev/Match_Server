package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationTemporaryConvertor;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationTemporaryService {
   private final DonationTemporaryConvertor donationTemporaryConvertor;
    public DonationTemporaryRes.UserInfo getUserInfo(User user) {
        return donationTemporaryConvertor.UserInfo(user);
    }
}
