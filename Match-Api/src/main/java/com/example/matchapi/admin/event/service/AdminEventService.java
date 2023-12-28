package com.example.matchapi.admin.event.service;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchapi.common.util.MessageHelper;
import com.example.matchapi.event.converter.EventConverter;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.constants.enums.Topic;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.event.adaptor.EventAdaptor;
import com.example.matchdomain.event.adaptor.EventContentAdaptor;
import com.example.matchdomain.event.entity.Event;
import com.example.matchdomain.event.entity.EventContent;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchcommon.constants.MatchAlertStatic.EVENT_UPLOAD_BODY;
import static com.example.matchdomain.common.model.ContentsType.IMG;
import static com.example.matchdomain.common.model.ContentsType.TEXT;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventConverter eventConverter;
    private final MessageHelper messageHelper;
    private final EventAdaptor eventAdaptor;
    private final EventContentAdaptor eventContentAdaptor;
    private final S3UploadService s3UploadService;

    @Transactional
    @CacheEvict(value = "eventCache", allEntries = true, cacheManager = "ehcacheManager")
    public void uploadEventList(MultipartFile thumbnail, EventUploadReq eventUploadReq) {

        String thumbnailUrl = s3UploadService.uploadOneImg("event", thumbnail);
        Event event  = eventAdaptor.save(eventConverter.convertToEventUpload(eventUploadReq, thumbnailUrl));

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

        eventContentAdaptor.saveAll(eventContents);

        messageHelper.helpFcmMessage(EVENT_UPLOAD_BODY, Topic.EVENT_UPLOAD, event.getId());
    }


    @Transactional
    @CacheEvict(value = "eventCache", allEntries = true, cacheManager = "ehcacheManager")
    public void deleteEvent(Long eventId) {
        Event event = eventAdaptor.findByEvent(eventId);
        List<EventContent> eventContents = event.getEventContents();
        for(EventContent eventContent : eventContents){
            if(eventContent.getContentsType().equals(IMG)){
                s3UploadService.deleteFile(eventContent.getContents());
            }
        }
        eventAdaptor.deleteByEventId(eventId);
    }

    public PageResponse<List<EventRes.EventList>> getEventList(int page, int size) {
        Page<Event> events = eventAdaptor.findEvent(page, size);
        return new PageResponse<>(events.isLast(), events.getTotalPages(), eventConverter.convertToEventList(events.getContent()));
    }
}
