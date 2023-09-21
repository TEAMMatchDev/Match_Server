package com.example.matchinfrastructure.match_aligo.config;

import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(MatchAligoInfoErrorDecoder.class)
public class MatchAligoInfoConfig {
    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public MatchAligoInfoErrorDecoder commonFeignErrorDecoder() {
        return new MatchAligoInfoErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new feign.form.FormEncoder();
    }
}
