package com.example.matchapi.admin.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ADMIN-04-Order💸 관리자 결제 관련 API 입니다.", description = "결제 관리 API 입니다.")
public class AdminOrderController {

}
