package com.example.matchapi.event.controller;

import com.example.matchapi.event.dto.EventRes;
import com.example.matchapi.event.service.EventService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "12-Event 🎉 이벤트 관련 API")
public class EventController {
    private final EventService eventService;

    @GetMapping("")
    @Operation(summary = "12-01 이벤트 리스트 조회")
    public CommonResponse<PageResponse<List<EventRes.EventList>>> getEventLists(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        return CommonResponse.onSuccess(eventService.getEventList(page, size));
    }

}
