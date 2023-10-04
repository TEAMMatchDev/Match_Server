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
        RedisProperties.class,
        DiscordProperties.class,
        AligoProperties.class,
        AwsS3Properties.class,
        EmailPasswordProperties.class,
        MatchAligoUrl.class,
        ServerHostProperties.class,
        WebReturnUrl.class
})
@Configuration
public class ConfigurationPropertiesConfig {}
