package com.example.matchdomain.event.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class EventAdaptor {
    private final EventRepository eventRepository;
}
