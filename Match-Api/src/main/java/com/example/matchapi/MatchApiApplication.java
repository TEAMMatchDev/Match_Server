package com.example.matchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackages = {"com.example.matchapi","com.example.matchdomain"})
@EntityScan(basePackages = {"com.example.matchdomain"})
@EnableJpaRepositories(basePackages = {"com.example.matchdomain"})

public class MatchApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchApiApplication.class, args);
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("MATCH API Server - HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
        //
    }
}
