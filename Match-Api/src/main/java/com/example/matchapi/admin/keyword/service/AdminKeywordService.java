package com.example.matchapi.admin.keyword.service;

import com.example.matchapi.admin.keyword.converter.AdminKeywordConverter;
import com.example.matchapi.admin.keyword.dto.AdminKeywordReq;
import com.example.matchapi.keword.converter.KeywordConverter;
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
    private final KeywordConverter keywordConverter;
    private final AdminKeywordConverter adminKeywordConverter;

    @Transactional
    @CachePut(cacheNames = "keywordList", key = "'all'", cacheManager = "ehcacheManager")
    public List<KeywordRes.KeywordList> postKeyword(AdminKeywordReq.KeywordUpload keyword) {
        searchKeywordRepository.save(adminKeywordConverter.convertToKeyword(keyword));
        return cachingKeywordList();
    }

    @CacheEvict(cacheNames = "keywordList", cacheManager = "ehcacheManager")
    public List<KeywordRes.KeywordList> cachingKeywordList(){
        List<SearchKeyword> searchKeywords = searchKeywordRepository.findAllByOrderByPriorityAsc();
        List<KeywordRes.KeywordList> keywordLists = new ArrayList<>();

        searchKeywords.forEach(
                result -> keywordLists.add(keywordConverter.convertToKeywordList(result))
        );
        return keywordLists;
    }

    @CacheEvict(cacheNames = "keywordList", cacheManager = "ehcacheManager")
    public void deleteKeyword(Long keywordId) {
        searchKeywordRepository.deleteById(keywordId);
    }
}
