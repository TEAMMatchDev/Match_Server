package com.example.matchdomain.redis.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash(value = "code_auth")
public class CodeAuth {
    @Id
    private String auth;

    private String code;


    @TimeToLive
    private long ttl;

    public CodeAuth update(String auth,String code, long ttl) {
        this.auth = auth;
        this.code = code;
        this.ttl = ttl;
        return this;
    }
}