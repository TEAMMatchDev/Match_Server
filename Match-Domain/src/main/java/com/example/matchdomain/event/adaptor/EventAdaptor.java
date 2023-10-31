package com.example.matchdomain.event.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.event.entity.Event;
import com.example.matchdomain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.example.matchdomain.event.exception.GetEventErrorCode.NOT_EXIST_EVENT;

@Adaptor
@RequiredArgsConstructor
public class EventAdaptor {
    private final EventRepository eventRepository;

    public Page<Event> findEvent(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Event findByEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new BadRequestException(NOT_EXIST_EVENT));
    }
}
