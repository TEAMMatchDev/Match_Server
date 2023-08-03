package com.example.adminapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackages = {"com.example.adminapi","com.example.matchdomain","com.example.matchinfrastructure", "com.example.matchcommon"})
@OpenAPIDefinition(servers = {@Server(url = "${server.servlet.context-path}", description = "Default Server URL")})
@RequiredArgsConstructor
@Slf4j
public class AdminApiApplication implements ApplicationListener<ApplicationReadyEvent> {
    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
        long heapSize = Runtime.getRuntime().totalMemory();
        log.info("ADMIN API Server - HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){
        log.info("applicationReady status Active Profiles =" + Arrays.toString(environment.getActiveProfiles()));
    }


}
