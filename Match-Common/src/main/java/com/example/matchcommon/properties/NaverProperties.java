package com.example.matchcommon.properties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("oauth")
public class NaverProperties {

    private OAuthSecret naver;


    @Getter
    @Setter
    public static class OAuthSecret {
        private String client;
        private String secret;
        private String redirectUrl;
    }


    public String getNaverClientId() {
        return naver.getClient();
    }

    public String getNaverRedirectUrl() {
        return naver.getRedirectUrl();
    }

    public String getNaverClientSecret() {
        return naver.getSecret();
    }

}
