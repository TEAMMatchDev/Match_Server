package com.example.matchapi.keword.controller;

import com.example.matchapi.keword.dto.KeywordRes;
import com.example.matchapi.keword.service.KeywordService;
import com.example.matchcommon.reponse.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
@Tag(name = "09-Keyword 추천 검색 키워드 🔎",description = "검색 키워드 API")
public class keywordController {
    private final KeywordService keywordService;

    @GetMapping("")
    @Operation(summary = "09-01 Keyword 추천 키워드 조회💸")
    public CommonResponse<List<KeywordRes.KeywordList>> getKeywordList(){
        return CommonResponse.onSuccess(keywordService.getKeywordList());
    }
}
