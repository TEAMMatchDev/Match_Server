package com.example.matchinfrastructure.discord.config;

import com.example.matchinfrastructure.oauth.kakao.config.KakaoInfoErrorDecoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(DiscordInfoErrorDecoder.class)
public class DiscordFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/json");
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new DiscordInfoErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
