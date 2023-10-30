package com.example.matchinfrastructure.pay.portone.service;

import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchcommon.constants.MatchStatic.REGULAR;

@Service
@RequiredArgsConstructor
public class PortOneService {
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConvertor portOneConvertor;

    public PortOneResponse<PortOneBillPayResponse> payBillKey(String bid, String orderId, Long amount, String projectName, String customerId){
        String accessToken = portOneAuthService.getToken();
        return portOneFeignClient.payWithBillKey(accessToken,  portOneConvertor.PayWithBillKey(bid, orderId, amount, projectName, customerId));
    }
}
