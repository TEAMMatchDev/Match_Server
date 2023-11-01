package com.example.matchinfrastructure.discord.config;

import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(DiscordInfoErrorDecoder.class)
public class DiscordInfoConfig {
    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public DiscordInfoErrorDecoder commonFeignErrorDecoder() {
        return new DiscordInfoErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new feign.form.FormEncoder();
    }
}
