package com.example.matchapi.notification.dto;

import lombok.*;

import java.util.List;

public class NotificationRes {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationDetail {
        private int unReadCount;

        private List<NotificationList> notificationLists;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationList{
        private Long notificationId;

        private String notificationType;

        private String title;

        private String notificationDate;

        private boolean isRead;
    }
}
