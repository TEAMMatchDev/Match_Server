package com.example.matchapi.donation.convertor;

import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import com.example.matchdomain.user.entity.User;

@Convertor
public class DonationTemporaryConvertor {
    public DonationTemporaryRes.UserInfo UserInfo(User user) {
        return DonationTemporaryRes.UserInfo
                .builder()
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }

    public DonationTemporary DonationInfo(User user, DonationTemporaryReq.DonationInfo donationInfo) {
        return DonationTemporary
                .builder()
                .userId(user.getId())
                .donationKind(donationInfo.getDonationKind())
                .alarmMethod(donationInfo.getAlarmMethod())
                .phoneNumber(donationInfo.getPhoneNumber())
                .name(donationInfo.getUsername())
                .build();
    }
}
