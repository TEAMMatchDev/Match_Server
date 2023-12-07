package com.example.matchapi.portone.dto;

import com.example.matchapi.order.dto.OrderReq;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.user.entity.User;
import lombok.*;

public class PaymentCommand {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentValidation{
        private OrderRequest orderRequest;

        private User user;

        private Project project;

        private PaymentReq.ValidatePayment validatePayment;
    }
}
