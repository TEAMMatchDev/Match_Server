package com.example.matchinfrastructure.oauth.apple.client;

import com.example.matchinfrastructure.oauth.apple.config.AppleInfoConfig;
import com.example.matchinfrastructure.oauth.apple.dto.ApplePublicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "AppleFeignClient",
        url = "https://appleid.apple.com",
        configuration = AppleInfoConfig.class)
@Component
public interface AppleFeignClient {
    @GetMapping("/auth/keys")
    ApplePublicResponse getPublicKey();
}
