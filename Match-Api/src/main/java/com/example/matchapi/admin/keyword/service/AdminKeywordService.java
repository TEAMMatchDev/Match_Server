package com.example.matchapi.admin.keyword.service;

import com.example.matchapi.admin.keyword.convertor.AdminKeywordConvertor;
import com.example.matchapi.admin.keyword.dto.AdminKeywordReq;
import com.example.matchapi.keword.convertor.KeywordConvertor;
import com.example.matchapi.keword.dto.KeywordRes;
import com.example.matchdomain.keyword.entity.SearchKeyword;
import com.example.matchdomain.keyword.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminKeywordService {
    private final SearchKeywordRepository searchKeywordRepository;
    private final KeywordConvertor keywordConvertor;
    private final AdminKeywordConvertor adminKeywordConvertor;

    @Transactional
    @CachePut(cacheNames = "keywordList", key = "'all'")
    public List<KeywordRes.KeywordList> postKeyword(AdminKeywordReq.KeywordUpload keyword) {
        searchKeywordRepository.save(adminKeywordConvertor.Keyword(keyword));
        return cachingKeywordList();
    }

    @CacheEvict(cacheNames = "keywordList")
    public List<KeywordRes.KeywordList> cachingKeywordList(){
        List<SearchKeyword> searchKeywords = searchKeywordRepository.findAllByOrderByPriorityAsc();
        List<KeywordRes.KeywordList> keywordLists = new ArrayList<>();

        searchKeywords.forEach(
                result -> keywordLists.add(keywordConvertor.KeywordList(result))
        );
        return keywordLists;
    }
}
