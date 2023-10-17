package com.example.matchinfrastructure.oauth.apple.config;

import com.example.matchinfrastructure.oauth.kakao.config.KakaoInfoErrorDecoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(AppleInfoErrorDecoder.class)
public class AppleFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/json");
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new AppleInfoErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
