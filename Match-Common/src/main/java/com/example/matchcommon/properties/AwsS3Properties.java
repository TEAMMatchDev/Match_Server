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
@ConfigurationProperties("aws")
public class AwsS3Properties {
    private String accessKey;
    private String secretKey;
    private S3 s3;
    @Getter
    @Setter
    private static class S3{
        private String bucket;
        private String baseUrl;
    }

}
