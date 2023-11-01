package com.example.matchinfrastructure.aligo.config;

import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(AligoInfoErrorDecoder.class)
public class AligoInfoConfig {
    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public AligoInfoErrorDecoder commonFeignErrorDecoder() {
        return new AligoInfoErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new feign.form.FormEncoder();
    }
}
