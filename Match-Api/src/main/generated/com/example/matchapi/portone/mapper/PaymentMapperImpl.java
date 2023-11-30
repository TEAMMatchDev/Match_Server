package com.example.matchapi.portone.mapper;

import com.example.matchapi.portone.dto.PaymentCommand;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.pay.portone.dto.PortOneWebhook;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-30T13:55:54+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.19 (Oracle Corporation)"
)
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentCommand.PaymentValidation toPaymentValidationCommand(OrderRequest orderRequest, User user, Project project, PaymentReq.ValidatePayment validatePayment) {
        if ( orderRequest == null && user == null && project == null && validatePayment == null ) {
            return null;
        }

        PaymentCommand.PaymentValidation.PaymentValidationBuilder paymentValidation = PaymentCommand.PaymentValidation.builder();

        paymentValidation.orderRequest( orderRequest );
        paymentValidation.user( user );
        paymentValidation.project( project );
        paymentValidation.validatePayment( validatePayment );

        return paymentValidation.build();
    }

    @Override
    public PaymentCommand.PaymentValidation toCheckValidationCommand(OrderRequest orderRequest, User user, Project project, PortOneWebhook portOneWebhook) {
        if ( orderRequest == null && user == null && project == null && portOneWebhook == null ) {
            return null;
        }

        PaymentCommand.PaymentValidation.PaymentValidationBuilder paymentValidation = PaymentCommand.PaymentValidation.builder();

        paymentValidation.orderRequest( orderRequest );
        paymentValidation.user( user );
        paymentValidation.project( project );

        return paymentValidation.build();
    }
}
