package com.example.matchcommon.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@ConstructorBinding
@Component
@ConfigurationProperties("nice")
public class NicePayProperties {
    private String secret;
    private String client;
    private String url;

}
