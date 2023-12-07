package com.example.matchinfrastructure.aligo.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(AligoInfoErrorDecoder.class)
public class AligoKakaoConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new AligoInfoErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
