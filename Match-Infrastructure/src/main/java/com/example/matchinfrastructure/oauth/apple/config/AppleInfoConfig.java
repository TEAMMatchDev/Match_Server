package com.example.matchinfrastructure.oauth.apple.config;


import com.example.matchinfrastructure.oauth.kakao.config.KakaoInfoErrorDecoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(AppleInfoErrorDecoder.class)
public class AppleInfoConfig {

    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public AppleInfoErrorDecoder commonFeignErrorDecoder() {
        return new AppleInfoErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new feign.form.FormEncoder();
    }
}
