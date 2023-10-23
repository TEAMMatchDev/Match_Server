package com.example.matchapi.event.service;

import com.example.matchapi.event.convetor.EventConvertor;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.event.adaptor.EventAdaptor;
import com.example.matchdomain.event.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventConvertor eventConvertor;
    private final EventAdaptor eventAdaptor;

    public PageResponse<List<EventRes.EventList>> getEventList(int page, int size) {
        Page<Event> events = eventAdaptor.findEvent(page, size);
        return new PageResponse<>(events.isLast(), events.getTotalElements(), eventConvertor.EventList(events.getContent()));
    }

    public EventRes.EventDetail getEventDetail(Long eventId) {
        Event event = eventAdaptor.findByEvent(eventId);
        return eventConvertor.EventDetail(event);
    }
}
