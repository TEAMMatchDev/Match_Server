package com.example.matchinfrastructure.match_aligo.client;

import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.match_aligo.config.MatchAligoFeignConfiguration;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "MatchAligoFeignClient",
        url = "${match.aligo.url}",
        configuration = MatchAligoFeignConfiguration.class)
public interface MatchAligoFeignClient {
    @GetMapping("/send")
    CommonResponse<String> sendSmsAuth(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                       @RequestParam("phone") String phone,
                                       @RequestParam("code") String code);


    @PostMapping("/send/alim-talk")
    CommonResponse<String> sendAlimTalk(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                        @RequestParam("alimType")AlimType alimType,
                                        @RequestBody AlimTalkDto alimTalkDto);

}
