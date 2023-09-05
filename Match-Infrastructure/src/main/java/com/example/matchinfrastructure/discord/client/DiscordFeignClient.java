package com.example.matchinfrastructure.discord.client;

import com.example.matchinfrastructure.discord.config.DiscordFeignConfiguration;
import com.example.matchinfrastructure.discord.config.DiscordInfoConfig;
import com.example.matchinfrastructure.discord.dto.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "DiscordFeignClient",
        url = "https://discord.com/api/webhooks",
        configuration = DiscordFeignConfiguration.class)
@Component
public interface DiscordFeignClient {
    @PostMapping("${discord.webhook.error}")
    String errorMessage(Message message);

    @PostMapping("${discord.webhook.alert}")
    String alertMessage(Message message);
}
