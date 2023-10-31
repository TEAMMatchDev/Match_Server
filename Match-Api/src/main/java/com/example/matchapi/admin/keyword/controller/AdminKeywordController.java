package com.example.matchapi.admin.keyword.controller;

import com.example.matchapi.admin.keyword.dto.AdminKeywordReq;
import com.example.matchapi.admin.keyword.service.AdminKeywordService;
import com.example.matchapi.keword.dto.KeywordRes;
import com.example.matchapi.keword.service.KeywordService;
import com.example.matchcommon.reponse.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/keywords")
@Tag(name = "ADMIN-07-Keyword 추천 검색 키워드 🔎",description = "검색 키워드 API")
public class AdminKeywordController {
    private final KeywordService keywordService;
    private final AdminKeywordService adminKeywordService;

    @GetMapping("")
    @Operation(summary = "07-01 ADMIN Keyword 추천 조회🔎")
    public CommonResponse<List<KeywordRes.KeywordList>> getKeywordList(){
        return CommonResponse.onSuccess(keywordService.getKeywordList());
    }

    @PostMapping("")
    @Operation(summary = "07-02 ADMIN Keyword 업로드🔎")
    public CommonResponse<List<KeywordRes.KeywordList>> postKeywordList(
            @RequestBody AdminKeywordReq.KeywordUpload keyword
            ){

        return CommonResponse.onSuccess(adminKeywordService.postKeyword(keyword));
    }
}
