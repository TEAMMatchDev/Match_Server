package com.example.matchinfrastructure.pay.nice.client;

import com.example.matchinfrastructure.pay.nice.config.NiceFeignConfiguration;
import com.example.matchinfrastructure.pay.nice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "NiceAuthFeignClient",
        url = "${nice.url}",
        configuration = NiceFeignConfiguration.class)
@Component
public interface NiceAuthFeignClient {

    @PostMapping("/v1/payments/{tid}")
    NicePaymentAuth paymentAuth(@RequestHeader(name = "Authorization") String Authorization,
                                @PathVariable("tid") String tid,
                                @RequestBody NicePayRequest nicePayRequest);

    @PostMapping("/v1/payments/{tid}/cancel")
    NicePaymentAuth cancelPayment(@RequestHeader(name = "Authorization") String Authorization,
                                  @PathVariable("tid") String tid,
                                  @RequestBody NicePayCancelRequest nicePayCancelRequest
                                  );

    @PostMapping("/v1/subscribe/regist")
    NicePayBillkeyResponse registrationCard(@RequestHeader(name = "Authorization") String Authorization,
                                            @RequestBody NicePayRegistrationCardRequest nicePayRegistrationCardRequest);

    @PostMapping("/v1/subscribe/{bid}/payments")
    NiceBillOkResponse billOkRequest(@RequestHeader(name = "Authorization") String Authorization,
                                     @PathVariable("bid") String bid,
                                     @RequestBody NiceBillOkRequest niceBillOkRequest);
}
