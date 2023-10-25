package com.example.matchapi.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/terms")
public class HtmlController {
    @GetMapping("/policy")
    public String showPrivacyPolicy(){
        return "PrivacyPolicy";
    }

    @GetMapping("/service")
    public String showTermsConditions(){
        return "TermsConditions";
    }
}
