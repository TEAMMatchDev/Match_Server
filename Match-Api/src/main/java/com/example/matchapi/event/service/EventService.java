package com.example.matchapi.event.service;

import com.example.matchapi.event.converter.EventConverter;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.event.adaptor.EventAdaptor;
import com.example.matchdomain.event.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventConverter eventConverter;
    private final EventAdaptor eventAdaptor;

    @Cacheable(value = "eventCache", key = "{#page, #size}", cacheManager = "ehcacheManager")
    public PageResponse<List<EventRes.EventList>> getEventList(int page, int size) {
        Page<Event> events = eventAdaptor.findEvent(page, size);
        List<EventRes.EventList> eventLists = cachingEventLists(events.getContent());
        return new PageResponse<>(events.isLast(), events.getTotalElements(), eventLists);
    }

    public List<EventRes.EventList> cachingEventLists(List<Event> content) {
        return eventConverter.convertToEventList(content);
    }


    public EventRes.EventDetail getEventDetail(Long eventId) {
        Event event = eventAdaptor.findByEvent(eventId);
        return eventConverter.convertToEventDetail(event);
    }
}
