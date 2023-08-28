package com.example.matchdomain.redis.repository;

import com.example.matchdomain.redis.entity.OrderRequest;
import org.springframework.data.repository.CrudRepository;


public interface OrderRequestRepository extends CrudRepository<OrderRequest, String> {
}
