package com.example.matchapi.event.dto;

import lombok.*;

import java.time.LocalDate;

public class EventRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventList {
        private Long eventId;

        private String title;

        private String smallTitle;

        private String eventStatus;

        private LocalDate startDate;

        private LocalDate endDate;
    }
}
