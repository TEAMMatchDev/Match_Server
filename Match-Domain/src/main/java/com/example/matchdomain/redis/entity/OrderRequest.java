package com.example.matchdomain.redis.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash(value = "order_request")
public class OrderRequest {
    @Id
    private String orderId;

    private String userId;

    private String projectId;

    private int amount;




    @TimeToLive
    private long ttl;

    public OrderRequest update(String orderId,String projectId, String userId, int amount, long ttl) {
        this.orderId = orderId;
        this.projectId = projectId;
        this.userId = userId;
        this.ttl = ttl;
        this.amount = amount;
        return this;
    }
}
