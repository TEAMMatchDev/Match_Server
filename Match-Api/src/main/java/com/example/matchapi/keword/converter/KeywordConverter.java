package com.example.matchapi.keword.converter;

import com.example.matchapi.keword.dto.KeywordRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.keyword.entity.SearchKeyword;

@Converter
public class KeywordConverter {
    public KeywordRes.KeywordList convertToKeywordList(SearchKeyword result) {
        return KeywordRes.KeywordList
                .builder()
                .keywordId(result.getId())
                .keyword(result.getKeyword())
                .priority(result.getPriority())
                .build();
    }
}
