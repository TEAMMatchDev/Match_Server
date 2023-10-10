package com.example.matchinfrastructure.pay.portone.service;

import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.dto.PortOneAuth;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOneAuthReq;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortOneAuthService {
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneProperties portOneProperties;

    public String getToken() {
        String token =  fetchPortOneToken();
        System.out.println(token);
        return token;
    }

    @CachePut(value = "portOneTokenCache")
    public String fetchPortOneToken() {
        PortOneResponse<PortOneAuth> portOneResponse = portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
        return portOneResponse.getResponse().getAccess_token();
    }

    @CachePut(value = "portOneTokenCache")
    public String refreshToken() {
        String newToken = fetchPortOneToken();
        System.out.println("refresh Token");
        return newToken;
    }

    //@Scheduled(fixedRate = 100)
    @Scheduled(fixedRate = 1700000) // 30분마다 실행 (1800초)
    public void scheduleTokenRefresh() {
        System.out.println("refresh token Schedule");
        String refreshToken = refreshToken();
    }

    public String getTokens() {
        PortOneResponse<PortOneAuth> portOneResponse = portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
        return portOneResponse.getResponse().getAccess_token();
    }
}

