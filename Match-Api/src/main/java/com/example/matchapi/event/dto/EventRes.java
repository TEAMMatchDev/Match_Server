package com.example.matchapi.event.dto;

import com.example.matchdomain.common.model.ContentsType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class EventRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventList {
        private Long eventId;

        private String thumbnail;

        private String title;

        private String smallTitle;

        private String eventStatus;

        private LocalDate startDate;

        private LocalDate endDate;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventDetail {
       private EventInfo eventInfo;

       private List<EventContents> eventContents;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventInfo{
        private Long eventId;

        private String title;

        private String smallTitle;

        private LocalDate startDate;

        private LocalDate endDate;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventContents{
        private Long contentId;

        private ContentsType contentsType;

        private String contents;
    }
}
