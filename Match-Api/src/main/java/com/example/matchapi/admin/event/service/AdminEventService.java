package com.example.matchapi.admin.event.service;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchapi.common.util.MessageHelper;
import com.example.matchapi.event.converter.EventConverter;
import com.example.matchapi.event.service.EventService;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.constants.enums.Topic;
import com.example.matchdomain.event.entity.Event;
import com.example.matchdomain.event.entity.EventContent;
import com.example.matchdomain.event.repository.EventContentRepository;
import com.example.matchdomain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchcommon.constants.MatchAlertStatic.EVENT_UPLOAD_BODY;
import static com.example.matchdomain.common.model.ContentsType.IMG;
import static com.example.matchdomain.common.model.ContentsType.TEXT;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final EventContentRepository eventContentRepository;
    private final EventConverter eventConverter;
    private final EventService eventService;
    private final MessageHelper messageHelper;
    @RedissonLock(LockName = "이벤트 업로드", key = "#eventUploadReq")
    @CacheEvict(value = "eventCache", allEntries = true, cacheManager = "ehcacheManager")
    public void uploadEventList(EventUploadReq eventUploadReq) {

        Event event  = eventRepository.save(eventConverter.convertToEventUpload(eventUploadReq, eventUploadReq.getThumbnail()));

        Long eventId = event.getId();

        List<EventContent> eventContents = new ArrayList<>();
        for(ContentsList content : eventUploadReq.getContentsList()){
            if(content.getContentsType().equals(IMG)){
                eventContents.add(eventConverter.convertToEventContents(eventId, content.getContents(), IMG));
            }
            else{
                eventContents.add(eventConverter.convertToEventContents(eventId, content.getContents(), TEXT));
            }
        }

        eventContentRepository.saveAll(eventContents);

        messageHelper.helpFcmMessage(EVENT_UPLOAD_BODY, Topic.EVENT_UPLOAD, event.getId());
    }


}
