package com.example.matchapi.keword.convertor;

import com.example.matchapi.keword.dto.KeywordRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.keyword.entity.SearchKeyword;

@Convertor
public class KeywordConvertor {
    public KeywordRes.KeywordList convertToKeywordList(SearchKeyword result) {
        return KeywordRes.KeywordList
                .builder()
                .keywordId(result.getId())
                .keyword(result.getKeyword())
                .priority(result.getPriority())
                .build();
    }
}
