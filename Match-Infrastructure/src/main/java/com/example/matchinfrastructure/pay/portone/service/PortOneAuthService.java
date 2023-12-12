package com.example.matchinfrastructure.pay.portone.service;

import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchinfrastructure.pay.portone.dto.PortOneAuth;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOneAuthReq;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortOneAuthService {
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneProperties portOneProperties;
    private final StringRedisTemplate stringRedisTemplate;


    public String getToken(String profile) {
        log.info("request token");
        String cachedToken = stringRedisTemplate.opsForValue().get("portOneTokenCache::" + profile);
        if (cachedToken != null) {
            return cachedToken;
        }

        return requestNewToken(profile);
    }

    public String requestNewToken(String profile) {
        PortOneResponse<PortOneAuth> portOneResponse = getPortOneToken();

        long ttl = portOneResponse.getResponse().getExpired_at()-portOneResponse.getResponse().getNow();

        log.info("token ttl : " + ttl);

        String newToken = portOneResponse.getResponse().getAccess_token();

        stringRedisTemplate.opsForValue().set("portOneTokenCache::" + profile, newToken, Duration.ofSeconds(ttl));

        return portOneResponse.getResponse().getAccess_token();
    }


    public String getAuthToken() {
        return getPortOneToken().getResponse().getAccess_token();
    }


    private PortOneResponse<PortOneAuth> getPortOneToken() {
        return portOneFeignClient.getAccessToken(PortOneAuthReq.builder().imp_key(portOneProperties.getKey()).imp_secret(portOneProperties.getSecret()).build());
    }

}

