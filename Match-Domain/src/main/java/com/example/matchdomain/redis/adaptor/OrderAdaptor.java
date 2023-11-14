package com.example.matchdomain.redis.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import lombok.RequiredArgsConstructor;

import static com.example.matchdomain.order.exception.PortOneAuthErrorCode.NOT_EXIST_ORDER_ID;

@Adaptor
@RequiredArgsConstructor
public class OrderAdaptor {
    private final OrderRequestRepository orderRequestRepository;

    public OrderRequest findById(String orderId) {
        return orderRequestRepository.findById(orderId).orElseThrow(()->new BadRequestException(NOT_EXIST_ORDER_ID));
    }

    public void deleteById(String orderId) {
        orderRequestRepository.deleteById(orderId);
    }
}
