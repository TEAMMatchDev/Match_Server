package com.example.matchapi.user.controller;

/*
import com.example.matchapi.security.JwtService;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.repository.UserAddressRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
@Api(tags = "00-Test✨")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserAddressRepository userAddressRepository;


    @GetMapping("")
    public String getUser(){
        System.out.println("hi");
        return "Success";
    }

    @GetMapping("/response")
    public CommonResponse<String> getTest(){
        Optional<User> user = userService.findUser(1L);
        //System.out.println(profile);
        return CommonResponse.onSuccess(user.get().getUsername());
    }

    @GetMapping("/jwt/{userId}")
    public CommonResponse<String> testCreatJwt(@PathVariable Long userId){
        return CommonResponse.onSuccess(jwtService.createToken(userId));
    }

    @GetMapping("/validate")
    public CommonResponse<List<UserRes.UserAddress>> testValidateJwt(@AuthenticationPrincipal User user){
        log.info("Validate JWT");
        List<UserAddress> userAddresses = userService.findUserAddress(user.getId());
        List<UserRes.UserAddress> userAddress = new ArrayList<>();
        userAddresses.forEach(
                result -> userAddress.add(
                        new UserRes.UserAddress(
                                result.getId(),
                                result.getUserId()
                        )
                )
        );

        System.out.println(userAddress);
        return CommonResponse.onSuccess(userAddress);
    }
}


 */