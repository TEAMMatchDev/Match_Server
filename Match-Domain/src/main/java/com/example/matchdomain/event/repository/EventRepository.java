package com.example.matchdomain.event.repository;

import com.example.matchdomain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
