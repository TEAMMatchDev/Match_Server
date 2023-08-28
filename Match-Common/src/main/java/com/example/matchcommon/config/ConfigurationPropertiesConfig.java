package com.example.matchcommon.config;

import com.example.matchcommon.properties.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties({
        KakaoProperties.class,
        JwtProperties.class,
        NaverProperties.class,
        CoolSmsProperties.class,
        NicePayProperties.class,
        RedisProperties.class
})
@Configuration
public class ConfigurationPropertiesConfig {}
