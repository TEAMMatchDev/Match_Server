package com.example.adminapi.user.converter;

import com.example.adminapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.user.repository.UserRepository;

@Converter
public class UserConverter {
    public UserRes.SignUpInfo UserSignUpInfo(Long oneDayUser, Long weekUser, Long monthUser, Long totalUser) {
        return UserRes.SignUpInfo.builder()
                .totalUserCnt(totalUser)
                .oneDayUserCnt(oneDayUser)
                .weekUserCnt(weekUser)
                .monthUserCnt(monthUser)
                .build();
    }

    public UserRes.UserList UserList(UserRepository.UserList result) {
        return UserRes.UserList
                .builder()
                .userId(result.getUserId())
                .name(result.getName())
                .birth(String.valueOf(result.getBirth()))
                .socialType(result.getSocialType().getName())
                .gender(result.getGender().getValue())
                .email(result.getEmail())
                .phoneNumber(result.getPhoneNumber())
                .donationCnt(result.getDonationCnt())
                .totalAmount(result.getTotalAmount())
                .card(result.getCard())
                .status(result.getStatus().getName())
                .createdAt(result.getCreatedAt().toString())
                .build();
    }
}
