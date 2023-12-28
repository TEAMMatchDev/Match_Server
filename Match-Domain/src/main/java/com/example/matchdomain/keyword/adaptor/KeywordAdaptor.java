package com.example.matchdomain.keyword.adaptor;

import static com.example.matchdomain.keyword.exception.KeywordErrorCode.*;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.keyword.entity.SearchKeyword;
import com.example.matchdomain.keyword.repository.SearchKeywordRepository;

import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class KeywordAdaptor {
	private final SearchKeywordRepository searchKeywordRepository;

	public SearchKeyword findByKeywordId(Long keywordId) {
		return searchKeywordRepository.findById(keywordId).orElseThrow(()-> new NotFoundException(KEYWORD_ERROR_CODE));
	}

	public SearchKeyword save(SearchKeyword searchKeyword) {
		return searchKeywordRepository.save(searchKeyword);
	}
}
