package com.example.matchapi.event.convetor;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.event.entity.Event;
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
                    eventLists.add(EventDetail(result));
                }
        );
        return eventLists;
    }

    private EventRes.EventList EventDetail(Event result) {
        return EventRes.EventList
                .builder()
                .eventId(result.getId())
                .title(result.getTitle())
                .smallTitle(result.getSmallTitle())
                .eventStatus(timeHelper.checkFinishStatus(result.getEventEndDate()))
                .startDate(result.getEventStartDate())
                .endDate(result.getEventEndDate())
                .build();
    }
}
