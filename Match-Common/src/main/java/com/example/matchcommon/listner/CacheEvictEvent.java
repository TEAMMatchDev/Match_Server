package com.example.matchcommon.listner;

import org.springframework.context.ApplicationEvent;

public class CacheEvictEvent extends ApplicationEvent {
    private final Long userId;

    public CacheEvictEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
