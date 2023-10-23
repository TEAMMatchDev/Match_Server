package com.example.matchdomain.event.repository;

import com.example.matchdomain.event.entity.EventContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventContentRepository extends JpaRepository<EventContent, Long> {
}
