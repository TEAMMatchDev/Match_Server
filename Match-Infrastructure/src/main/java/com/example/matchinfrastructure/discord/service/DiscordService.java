package com.example.matchinfrastructure.discord.service;

import com.example.matchinfrastructure.discord.client.DiscordFeignClient;
import com.example.matchinfrastructure.discord.convertor.DiscordConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class DiscordService {
    private final DiscordFeignClient discordFeignClient;
    private final DiscordConvertor discordConvertor;
    @Async("discord-message")
    public void sendUnKnownMessage(String username, Exception exception, HttpServletRequest request) {
        discordFeignClient.errorMessage(discordConvertor.convertToUnknownMessage(username, exception, request));

    }

    @Async("discord-message")
    public void sendKnownErrorMessage(String message) {
        discordFeignClient.errorMessage(discordConvertor.convertToKnownMessage(message));
    }
}
