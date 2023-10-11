package com.example.matchapi.keword.service;

import com.example.matchapi.keword.convertor.KeywordConvertor;
import com.example.matchapi.keword.dto.KeywordRes;
import com.example.matchdomain.keyword.entity.SearchKeyword;
import com.example.matchdomain.keyword.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordConvertor keywordConvertor;
    private final SearchKeywordRepository searchKeywordRepository;


    public List<KeywordRes.KeywordList> getKeywordList() {
        List<SearchKeyword> searchKeywords = searchKeywordRepository.findAllByOrderByPriorityAsc();
        List<KeywordRes.KeywordList> keywordLists = new ArrayList<>();

        searchKeywords.forEach(
                result -> keywordLists.add(keywordConvertor.KeywordList(result))
        );
        return keywordLists;
    }
}
