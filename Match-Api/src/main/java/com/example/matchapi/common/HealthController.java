package com.example.matchapi.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check 👋")
public class HealthController {
    @GetMapping("")
    public String healthCheck(){
        return "I'm Healthy Server";
    }
}
