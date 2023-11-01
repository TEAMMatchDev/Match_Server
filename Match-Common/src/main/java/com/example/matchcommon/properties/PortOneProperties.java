package com.example.matchcommon.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@ConstructorBinding
@Component
@ConfigurationProperties("portone")
public class PortOneProperties {
    private String code;
    private String key;
    private String secret;
    private String billmid;
}
