package com.example.matchapi.event.convetor;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.common.model.ContentsType;
import com.example.matchdomain.event.entity.Event;
import com.example.matchdomain.event.entity.EventContent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Convertor
@RequiredArgsConstructor
public class EventConvertor {
    private final TimeHelper timeHelper;
    public List<EventRes.EventList> EventList(List<Event> content) {
        List<EventRes.EventList> eventLists = new ArrayList<>();

        content.forEach(
                result -> {
                    eventLists.add(EventListDetail(result));
                }
        );
        return eventLists;
    }

    private EventRes.EventList EventListDetail(Event result) {
        return EventRes.EventList
                .builder()
                .eventId(result.getId())
                .title(result.getTitle())
                .thumbnail(result.getThumbnail())
                .smallTitle(result.getSmallTitle())
                .eventStatus(timeHelper.checkFinishStatus(result.getEventEndDate()))
                .startDate(result.getEventStartDate())
                .endDate(result.getEventEndDate())
                .build();
    }

    public EventRes.EventDetail EventDetail(Event event) {
        EventRes.EventInfo eventInfo = EventInfo(event);
        List<EventRes.EventContents> eventContents = new ArrayList<>();

        event.getEventContents().forEach(
                result -> eventContents.add(EventContents(result))
        );

        return EventRes.EventDetail
                .builder()
                .eventInfo(eventInfo)
                .eventContents(eventContents)
                .build();
    }

    private EventRes.EventContents EventContents(EventContent result) {
        return EventRes.EventContents
                .builder()
                .contentId(result.getId())
                .contentsType(result.getContentsType())
                .contents(result.getContents())
                .build();
    }

    private EventRes.EventInfo EventInfo(Event event) {
        return EventRes.EventInfo
                .builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .smallTitle(event.getSmallTitle())
                .startDate(event.getEventStartDate())
                .endDate(event.getEventEndDate())
                .build();
    }

    public Event convertToEventUpload(EventUploadReq eventUploadReq, String thumbnail) {
        return Event
                .builder()
                .title(eventUploadReq.getTitle())
                .eventType(eventUploadReq.getEventType())
                .smallTitle(eventUploadReq.getSmallTitle())
                .thumbnail(thumbnail)
                .eventStartDate(eventUploadReq.getEventStartDate())
                .eventEndDate(eventUploadReq.getEventEndDate())
                .build();
    }

    public EventContent convertToEventContents(Long eventId, String contents, ContentsType contentsType) {
        return EventContent
                .builder()
                .eventId(eventId)
                .contentsType(contentsType)
                .contents(contents)
                .build();
    }
}
