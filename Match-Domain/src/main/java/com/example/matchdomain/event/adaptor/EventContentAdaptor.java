package com.example.matchdomain.event.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.event.entity.EventContent;
import com.example.matchdomain.event.repository.EventContentRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class EventContentAdaptor {
    private final EventContentRepository contentRepository;


    public void saveAll(List<EventContent> eventContents) {
        contentRepository.saveAll(eventContents);
    }

}
