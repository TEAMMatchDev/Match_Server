package com.example.adminapi.user.convertor;

import com.example.adminapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.user.repository.UserRepository;

@Convertor
public class UserConvertor {
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
                .socialType(result.getSocialType().getValue())
                .gender(result.getGender().getValue())
                .email(result.getEmail())
                .phoneNumber(result.getPhoneNumber())
                .donationCnt(result.getDonationCnt())
                .totalAmount(result.getTotalAmount())
                .card(result.getCard())
                .build();
    }
}
