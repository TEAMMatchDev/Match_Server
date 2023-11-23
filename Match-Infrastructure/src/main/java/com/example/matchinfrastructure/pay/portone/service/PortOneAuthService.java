package com.example.matchinfrastructure.pay.portone.service;

import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchinfrastructure.pay.portone.dto.PortOneAuth;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOneAuthReq;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortOneAuthService {
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneProperties portOneProperties;

    @Cacheable(value = "portOneTokenCache", key = "'all'")
    public String getToken() {
        String token =  fetchPortOneToken();
        log.info("request : " + token);
        return token;
    }

    public String fetchPortOneToken() {
        log.info("request token");
        return getTokens();
    }

    @CachePut(value = "portOneTokenCache", key = "'all'")
    public String getTokens() {
        PortOneResponse<PortOneAuth> portOneResponse = portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
        return portOneResponse.getResponse().getAccess_token();
    }

    public String getAuthToken() {
        PortOneResponse<PortOneAuth> portOneResponse = portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
        return portOneResponse.getResponse().getAccess_token();
    }

    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void refreshAuthToken() {
       String refreshToken  = getTokens();
       log.info("refresh token {} ", refreshToken);
    }
}

