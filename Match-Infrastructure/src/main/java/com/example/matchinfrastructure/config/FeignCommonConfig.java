package com.example.matchinfrastructure.config;


import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchinfrastructure.discord.BaseFeignDiscordPackage;
import com.example.matchinfrastructure.oauth.BaseFeignClientPackage;
import com.example.matchinfrastructure.pay.BasePayFeignClientPackage;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Logger.Level;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {BaseFeignClientPackage.class, BasePayFeignClientPackage.class, BaseFeignDiscordPackage.class})
public class FeignCommonConfig {

    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder(customObjectMapper());
    }

    /**
     * 타임관련 유닛 해석을 위한 디코더 추가
     *
     * @return
     */
    public ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        return objectMapper;
    }

    @Bean
    Level feignLoggerLevel() {
        return Level.FULL;
    }


}
