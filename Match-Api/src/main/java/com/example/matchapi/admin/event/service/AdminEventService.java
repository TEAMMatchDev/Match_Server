package com.example.matchapi.admin.event.service;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchapi.event.convetor.EventConvertor;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchapi.event.service.EventService;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchdomain.common.model.ContentsType;
import com.example.matchdomain.event.entity.Event;
import com.example.matchdomain.event.entity.EventContent;
import com.example.matchdomain.event.repository.EventContentRepository;
import com.example.matchdomain.event.repository.EventRepository;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchcommon.constants.MatchStatic.EVENT_S3_DIR;
import static com.example.matchdomain.common.model.ContentsType.IMG;
import static com.example.matchdomain.common.model.ContentsType.TEXT;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final EventContentRepository eventContentRepository;
    private final EventConvertor eventConvertor;
    private final EventService eventService;
    @RedissonLock(LockName = "이벤트 업로드", key = "#eventUploadReq")
    @CacheEvict(value = "eventCache", allEntries = true, cacheManager = "ehcacheManager")
    public void uploadEventList(EventUploadReq eventUploadReq) {

        Event event  = eventRepository.save(eventConvertor.convertToEventUpload(eventUploadReq, eventUploadReq.getThumbnail()));

        Long eventId = event.getId();

        List<EventContent> eventContents = new ArrayList<>();
        for(ContentsList content : eventUploadReq.getContentsList()){
            if(content.getContentsType().equals(IMG)){
                eventContents.add(eventConvertor.convertToEventContents(eventId, content.getContents(), IMG));
            }
            else{
                eventContents.add(eventConvertor.convertToEventContents(eventId, content.getContents(), TEXT));
            }
        }

        eventContentRepository.saveAll(eventContents);
    }

}
