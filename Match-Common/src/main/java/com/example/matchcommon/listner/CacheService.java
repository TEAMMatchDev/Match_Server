package com.example.matchcommon.listner;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;


    public void evictCache(Long userId){
        CacheEvictEvent event = new CacheEvictEvent(this, userId);

        eventPublisher.publishEvent(event);
    }


}
