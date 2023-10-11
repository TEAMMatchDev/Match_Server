package com.example.matchdomain.keyword.repository;

import com.example.matchdomain.keyword.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    List<SearchKeyword> findAllByOrderByPriorityAsc();
}
