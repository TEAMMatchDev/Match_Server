package com.example.matchinfrastructure.aligo.client;

import com.example.matchinfrastructure.aligo.config.AligoFeignConfiguration;
import com.example.matchinfrastructure.aligo.dto.SendReq;
import com.example.matchinfrastructure.aligo.dto.SendRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "AligoFeignClient",
        url = "https://apis.aligo.in",
        configuration = AligoFeignConfiguration.class)
public interface AligoFeignClient {
    @PostMapping("/send/")
    SendRes sendOneMsg(@RequestParam("key") String key,
                       @RequestParam("user_id") String userId,
                       @RequestParam("sender") String sender,
                       @RequestParam("receiver") String receiver,
                       @RequestParam("msg") String msg);
}
