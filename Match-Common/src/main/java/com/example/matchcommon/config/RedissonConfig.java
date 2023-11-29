package com.example.matchcommon.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedissonConfig {
    private final RedisProperties redisProperties;
    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        log.info("RedissonConfig " + profile + " profile");
        if (profile.equals("prod")||profile.equals("dev")) {
            config.useClusterServers()
                    .addNodeAddress(REDISSON_HOST_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort())
                    // 다른 클러스터 노드도 필요에 따라 추가
                    .setScanInterval(2000);
        } else {
            config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort());
        }
        return Redisson.create(config);
    }
}
