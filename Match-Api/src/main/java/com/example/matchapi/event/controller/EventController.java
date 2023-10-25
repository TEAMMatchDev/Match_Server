package com.example.matchapi.event.controller;

import com.example.matchapi.event.dto.EventRes;
import com.example.matchapi.event.service.EventService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.event.exception.GetEventErrorCode;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "12-Event 🎉 이벤트 관련 API", description = "이벤트 관련 API 입니다.")
public class EventController {
    private final EventService eventService;

    @GetMapping("")
    @Operation(summary = "12-01 이벤트 리스트 조회")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    public CommonResponse<PageResponse<List<EventRes.EventList>>> getEventLists(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        return CommonResponse.onSuccess(eventService.getEventList(page, size));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "12-02 이벤트 상세 조회")
    @ApiErrorCodeExample({GetEventErrorCode.class, UserAuthErrorCode.class})
    public CommonResponse<EventRes.EventDetail> getEventDetail(@PathVariable Long eventId){
        return CommonResponse.onSuccess(eventService.getEventDetail(eventId));
    }

}
