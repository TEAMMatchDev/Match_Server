package com.example.matchinfrastructure.pay.portone.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(PortOneInfoErrorDecoder.class)
public class PortOneFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/json");
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new PortOneInfoErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
