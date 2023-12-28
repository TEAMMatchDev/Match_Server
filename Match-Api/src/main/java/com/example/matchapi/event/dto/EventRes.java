package com.example.matchapi.event.dto;

import com.example.matchdomain.common.model.ContentsType;
import com.example.matchdomain.event.enums.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EventRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventList implements Serializable {
        private Long eventId;

        private String thumbnail;

        private String title;

        private String smallTitle;

        private String eventStatus;

        private EventType eventType;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
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

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
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
