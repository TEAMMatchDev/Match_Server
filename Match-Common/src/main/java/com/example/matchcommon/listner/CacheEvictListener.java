package com.example.matchcommon.listner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@Slf4j
public class CacheEvictListener {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @EventListener
    @Async("event-listener")
    public void onCacheEvict(CacheEvictEvent event){
        log.info("CacheEvictEvent received, userId: {}", event.getUserId());
        String pattern = "flameCache::" + profile + "," + event.getUserId() + ",*";
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            log.info("CacheEvictEvent delete keys: {}", keys);
            stringRedisTemplate.delete(keys);
        }
        log.info("CacheEvictEvent finished, userId: {}", event.getUserId());
    }
}
