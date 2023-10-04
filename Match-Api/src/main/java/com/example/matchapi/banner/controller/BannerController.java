package com.example.matchapi.banner.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banners")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "06-Banner💳", description = "배너 API")
public class BannerController {
}
