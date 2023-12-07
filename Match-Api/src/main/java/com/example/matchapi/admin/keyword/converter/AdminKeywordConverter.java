package com.example.matchapi.admin.keyword.converter;

import com.example.matchapi.admin.keyword.dto.AdminKeywordReq;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.keyword.entity.SearchKeyword;

@Converter
public class AdminKeywordConverter {
    public SearchKeyword convertToKeyword(AdminKeywordReq.KeywordUpload keyword) {
        return SearchKeyword.builder().keyword(keyword.getSearchKeyword()).priority(keyword.getPriority()).build();
    }
}
