package com.example.matchcommon.config;

import com.example.matchcommon.properties.JwtProperties;
import com.example.matchcommon.properties.OauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties({
        OauthProperties.class,
        JwtProperties.class
})
@Configuration
public class ConfigurationPropertiesConfig {}
