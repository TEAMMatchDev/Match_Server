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

    @Cacheable(value = "portOneTokenCache")
    public String getToken() {
        String token =  fetchPortOneToken();
        System.out.println("request : " + token);
        return token;
    }

    public String fetchPortOneToken() {
        System.out.println("request token");
        return getTokens();
    }

    public String getTokens() {
        PortOneResponse<PortOneAuth> portOneResponse = portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
        return portOneResponse.getResponse().getAccess_token();
    }
}

