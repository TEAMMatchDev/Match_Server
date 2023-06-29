package com.example.matchapi.controller;

import com.example.matchapi.common.response.CommonResponse;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/test")
@Api(tags = "00-Test✨")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    @GetMapping("")
    public String getUser(){
        System.out.println("hi");
        return "Success";
    }

    @GetMapping("/response")
    public CommonResponse<String> getTest(){
        Long id = Long.valueOf(1);
        Optional<User> user = userRepository.findById(id);
        System.out.println("hi");
        return CommonResponse.onSuccess(user.get().getUsername());

    }
}
