package com.example.matchbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.example.matchbatch","com.example.matchdomain","com.example.matchinfrastructure", "com.example.matchcommon"})
@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Slf4j
public class MatchBatchApplication{
    public static void main(String[] args) {
        SpringApplication.run(MatchBatchApplication.class, args);
    }

}
