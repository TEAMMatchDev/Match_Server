package com.example.matchinfrastructure.oauth.naver.config;

import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(NaverInfoErrorDecoder.class)
public class NaverInfoConfig {
    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public NaverInfoErrorDecoder commonFeignErrorDecoder() {
        return new NaverInfoErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new feign.form.FormEncoder();
    }
}
