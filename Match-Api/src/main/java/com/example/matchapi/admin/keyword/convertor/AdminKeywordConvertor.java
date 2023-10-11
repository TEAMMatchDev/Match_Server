package com.example.matchapi.admin.keyword.convertor;

import com.example.matchapi.admin.keyword.dto.AdminKeywordReq;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.keyword.entity.SearchKeyword;

@Convertor
public class AdminKeywordConvertor {
    public SearchKeyword Keyword(AdminKeywordReq.KeywordUpload keyword) {
        return SearchKeyword.builder().keyword(keyword.getSearchKeyword()).priority(keyword.getPriority()).build();
    }
}
