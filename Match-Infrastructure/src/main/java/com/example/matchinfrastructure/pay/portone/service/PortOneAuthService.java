package com.example.matchinfrastructure.pay.portone.service;

import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchinfrastructure.pay.portone.dto.PortOneAuth;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOneAuthReq;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortOneAuthService {
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneProperties portOneProperties;

    @Cacheable(value = "portOneTokenCache", key = "'all'")
    public String getToken() {
        String token =  fetchPortOneToken();
        System.out.println("request : " + token);
        return token;
    }

    public String fetchPortOneToken() {
        System.out.println("request token");
        return getTokens();
    }

    @CachePut(value = "portOneTokenCache", key = "'all'")
    public String getTokens() {
        PortOneResponse<PortOneAuth> portOneResponse = portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
        return portOneResponse.getResponse().getAccess_token();
    }
}

