package com.example.matchinfrastructure.pay.portone.service;

import static com.example.matchcommon.constants.MatchStatic.*;

import com.example.matchcommon.properties.PortOneProperties;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortOneService {
    private IamportClient iamportClient;
    private final PortOneProperties portOneProperties;

    @PostConstruct
    private void init() {
        this.iamportClient = new IamportClient(portOneProperties.getKey(), portOneProperties.getSecret());
    }


    public void refundPayment(String impUid) {
        try {
            IamportResponse<Payment> paymentIamportResponse  = iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
            System.out.println(paymentIamportResponse.getCode());
            System.out.println(paymentIamportResponse.getResponse());
        } catch (IamportResponseException | IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void refundPaymentOrderId(String orderId) {
        try {
            IamportResponse<Payment> paymentIamportResponse  = iamportClient.cancelPaymentByImpUid(new CancelData(orderId, false));
            System.out.println(paymentIamportResponse.getCode());
            System.out.println(paymentIamportResponse.getResponse());
        } catch (IamportResponseException | IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
