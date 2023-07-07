package com.example.matchcommon.config;

import com.example.matchcommon.properties.JwtProperties;
import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchcommon.properties.NaverProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties({
        KakaoProperties.class,
        JwtProperties.class,
        NaverProperties.class
})
@Configuration
public class ConfigurationPropertiesConfig {}
