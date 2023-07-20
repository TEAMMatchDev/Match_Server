package com.example.matchapi.project.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Api(tags = "03-Project", value = "프로젝트 모아보기 용 API 입니다.")
public class ProjectController {

}
