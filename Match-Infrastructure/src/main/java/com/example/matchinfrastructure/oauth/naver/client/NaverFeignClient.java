package com.example.matchinfrastructure.oauth.naver.client;

import com.example.matchinfrastructure.oauth.naver.config.NaverInfoConfig;
import com.example.matchinfrastructure.oauth.naver.dto.NaverAddressDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "NaverFeignClient",
        url = "https://openapi.naver.com",
        configuration = NaverInfoConfig.class)
public interface NaverFeignClient {
    @GetMapping("/v1/nid/me")
    NaverUserInfoDto getInfo(@RequestHeader(name = "Authorization") String Authorization);

    @GetMapping("/v1/naverpay/address")
    NaverAddressDto getUserAddress(@RequestHeader(name = "Authorization") String Authorization);
}
