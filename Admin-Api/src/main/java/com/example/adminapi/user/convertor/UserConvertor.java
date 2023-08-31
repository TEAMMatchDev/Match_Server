package com.example.adminapi.user.convertor;

import com.example.adminapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Convertor;

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
}
