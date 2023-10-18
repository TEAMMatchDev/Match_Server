package com.example.matchapi.user.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchinfrastructure.user.client.NickNameFeignClient;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Helper
@RequiredArgsConstructor
public class UserHelper {
    private final NickNameFeignClient nickNameFeignClient;

    private static NickNameFeignClient staticNickNameFeignClient;


    @PostConstruct
    void init() {
        staticNickNameFeignClient = this.nickNameFeignClient;
    }

    public String birthConversion(LocalDate birth) {
        return birth.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String createRandomNickName(){
        return staticNickNameFeignClient.getNickName().getWords().get(0);
    }
}
