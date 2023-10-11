package com.example.matchdomain.keyword.repository;

import com.example.matchdomain.keyword.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
}
