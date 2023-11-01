package com.example.matchapi.donation.convertor;

import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donationTemporary.entity.Deposit;
import com.example.matchdomain.donationTemporary.entity.DonationList;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import com.example.matchdomain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Convertor
public class DonationTemporaryConvertor {
    public DonationTemporaryRes.UserInfo convertToUserInfo(User user) {
        return DonationTemporaryRes.UserInfo
                .builder()
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }

    public DonationTemporary convertToDonationInfo(User user, DonationTemporaryReq.DonationInfo donationInfo) {
        return DonationTemporary
                .builder()
                .userId(user.getId())
                .donationKind(donationInfo.getDonationKind())
                .alarmMethod(donationInfo.getAlarmMethod())
                .phoneNumber(donationInfo.getPhoneNumber())
                .name(donationInfo.getUsername())
                .deposit(Deposit.NONEXISTENCE)
                .build();
    }

    public DonationTemporaryRes.DonationList convertToDonationList(DonationList result, String amount) {
        return DonationTemporaryRes.DonationList
                .builder()
                .name(result.getName())
                .amount(amount)
                .donationDate(result.getCreatedAt().getYear()+"."+result.getCreatedAt().getMonthValue()+"."+result.getCreatedAt().getDayOfMonth()+"."+result.getCreatedAt().getHour()+"."+result.getCreatedAt().getMinute())
                .name(convertToReplaceMiddleWithAsterisk(result.getName()))
                .build();
    }

    public String convertToReplaceMiddleWithAsterisk(String str) {
        int length = str.length();

        if (length == 2) {
            // 문자열 길이가 2인 경우는 마지막 글자만 '*'로 대체
            return str.substring(0, 1) + "*";
        }
            // 문자열 길이가 2보다 큰 경우는 중간 부분을 '*'로 대체
        int middle = length / 2;
        return str.substring(0, middle) + "*" + str.substring(middle + 1);
    }

    public List<DonationTemporaryRes.DonationRequestAdminList> convertToDonationRequestAdminList(List<DonationTemporary> content) {
        List<DonationTemporaryRes.DonationRequestAdminList> donationRequestAdminLists = new ArrayList<>();
        content.forEach(
                result -> donationRequestAdminLists.add(
                        convertToDonationInfoList(result)
                )
        );
        return donationRequestAdminLists;
    }

    public DonationTemporaryRes.DonationRequestAdminList convertToDonationInfoList(DonationTemporary result) {
        return DonationTemporaryRes.DonationRequestAdminList
                .builder()
                .donationRequestId(result.getId())
                .username(result.getName())
                .phoneNumber(result.getPhoneNumber())
                .email(result.getEmail())
                .alarmMethod(result.getAlarmMethod().getName())
                .donationKind(result.getDonationKind().getName())
                .deposit(result.getDeposit().getName())
                .createdAt(result.getCreatedAt().toString())
                .build();
    }

    public DonationList convertToDonationDeposit(DonationTemporaryReq.DonationDeposit donationDeposit) {
        return DonationList.builder()
                .donationTemporaryId(donationDeposit.getDonationRequestId())
                .amount(donationDeposit.getAmount())
                .name(donationDeposit.getName())
                .build();
    }

    public DonationTemporaryRes.DonationDetail convertToDonationInfoDetail(DonationTemporary donationTemporary) {
        return DonationTemporaryRes.DonationDetail
                .builder()
                .donationRequestId(donationTemporary.getId())
                .username(donationTemporary.getName())
                .userId(donationTemporary.getUserId())
                .phoneNumber(donationTemporary.getPhoneNumber())
                .email(donationTemporary.getEmail())
                .alarmMethod(donationTemporary.getAlarmMethod().getName())
                .donationKind(donationTemporary.getDonationKind().getName())
                .deposit(donationTemporary.getDeposit().getName())
                .createdAt(donationTemporary.getCreatedAt().toString())
                .build();
    }

    public DonationTemporary convertToDonationInfoEmail(User user, DonationTemporaryReq.DonationInfo donationInfo) {
        return DonationTemporary
                .builder()
                .userId(user.getId())
                .donationKind(donationInfo.getDonationKind())
                .alarmMethod(donationInfo.getAlarmMethod())
                .email(donationInfo.getEmail())
                .name(donationInfo.getUsername())
                .deposit(Deposit.NONEXISTENCE)
                .build();
    }
}
