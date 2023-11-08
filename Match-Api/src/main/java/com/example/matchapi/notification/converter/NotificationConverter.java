package com.example.matchapi.notification.converter;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.notification.dto.NotificationRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notification.entity.Notification;
import com.example.matchdomain.notification.enums.NotificationType;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class NotificationConverter {
    private final TimeHelper timeHelper;
    public List<NotificationRes.NotificationList> convertToNotificationList(List<Notification> notifications) {
        List<NotificationRes.NotificationList> notificationLists = new ArrayList<>();

        notifications.forEach(
                result -> notificationLists.add(
                        convertToNotificationDetail(result)
                )
        );

        return notificationLists;
    }

    private NotificationRes.NotificationList convertToNotificationDetail(Notification result) {

        return NotificationRes.NotificationList
                .builder()
                .notificationId(result.getId())
                .notificationType(result.getNotificationType().getType())
                .title(result.getTitle())
                .notificationDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .isRead(result.getIsRead())
                .build();
    }

    public Notification convertToNotificationTest(User user, FCMNotificationRequestDto fcmNotificationRequestDto) {
        return Notification
                .builder()
                .body(fcmNotificationRequestDto.getBody())
                .notificationType(NotificationType.TEST)
                .userId(user.getId())
                .title(fcmNotificationRequestDto.getTitle())
                .build();
    }

    public NotificationRes.NotificationDetail convertNotificationDetail(Notification notification) {
        return NotificationRes.NotificationDetail
                .builder()
                .notificationId(notification.getId())
                .notificationType(notification.getNotificationType().getType())
                .title(notification.getTitle())
                .body(notification.getBody())
                .notificationDate(timeHelper.matchTimeFormat(notification.getCreatedAt()))
                .build();
    }
}
