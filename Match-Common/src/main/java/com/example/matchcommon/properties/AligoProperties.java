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
@ConfigurationProperties("aligo")
public class AligoProperties {
    private String username;
    private String key;
    private String sender;
    private String senderKey;
}
