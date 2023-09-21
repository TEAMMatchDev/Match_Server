package com.example.matchinfrastructure.match_aligo.client;

import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.match_aligo.config.MatchAligoFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "MatchAligoFeignClient",
        url = "${match.aligo.url}",
        configuration = MatchAligoFeignConfiguration.class)
public interface MatchAligoFeignClient {
    @GetMapping("/send")
    CommonResponse<String> sendSmsAuth(@RequestHeader(name = "X-AUTH-TOKEN") String token, @RequestParam("phone") String phone, @RequestParam("code") String code);


}
