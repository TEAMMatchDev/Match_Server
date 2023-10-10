package com.example.matchinfrastructure.pay.portone.config;


import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(PortOneInfoErrorDecoder.class)
public class PortOneInfoConfig {

    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public PortOneInfoErrorDecoder commonFeignErrorDecoder() {
        return new PortOneInfoErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new feign.form.FormEncoder();
    }
}
