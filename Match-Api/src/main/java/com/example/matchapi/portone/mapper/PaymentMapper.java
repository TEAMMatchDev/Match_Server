package com.example.matchapi.portone.mapper;

import com.example.matchapi.portone.dto.PaymentCommand;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentCommand.PaymentValidation toPaymentValidationCommand(OrderRequest orderRequest, User user, Project project, PaymentReq.ValidatePayment validatePayment);
}
