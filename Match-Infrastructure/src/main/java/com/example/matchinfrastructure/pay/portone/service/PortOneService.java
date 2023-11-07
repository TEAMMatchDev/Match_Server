package com.example.matchinfrastructure.pay.portone.service;

import com.example.matchcommon.properties.PortOneProperties;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PortOneService {
    private final IamportClient iamportClient;
    private final PortOneProperties portOneProperties;

    public PortOneService(PortOneProperties portOneProperties) {
        this.portOneProperties = portOneProperties;
        this.iamportClient = new IamportClient(portOneProperties.getKey(), portOneProperties.getSecret());

    }


    public void refundPayment(String impUid) {
        try {
            iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
