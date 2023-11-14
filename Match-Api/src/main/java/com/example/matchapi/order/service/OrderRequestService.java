package com.example.matchapi.order.service;

import com.example.matchdomain.redis.adaptor.OrderAdaptor;
import com.example.matchdomain.redis.entity.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRequestService {
    private final OrderAdaptor orderAdaptor;
    public OrderRequest findByOrderIdForPayment(String orderId) {
        OrderRequest orderRequest = orderAdaptor.findById(orderId);

        orderAdaptor.deleteById(orderId);

        return orderRequest;
    }

}
