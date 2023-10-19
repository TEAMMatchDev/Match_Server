package com.example.matchapi.notification.convertor;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.notification.dto.NotificationRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notification.entity.Notification;
import com.example.matchdomain.notification.enums.NotificationType;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Convertor
@RequiredArgsConstructor
public class NotificationConvertor {
    private final TimeHelper timeHelper;
    public List<NotificationRes.NotificationList> NotificationList(List<Notification> notifications) {
        List<NotificationRes.NotificationList> notificationLists = new ArrayList<>();

        notifications.forEach(
                result -> notificationLists.add(
                        NotificationDetail(result)
                )
        );

        return notificationLists;
    }

    private NotificationRes.NotificationList NotificationDetail(Notification result) {

        return NotificationRes.NotificationList
                .builder()
                .notificationId(result.getId())
                .notificationType(result.getNotificationType().getType())
                .title(result.getTitle())
                .notificationDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .isRead(result.getIsRead())
                .build();
    }

    public Notification NotificationTest(User user, FCMNotificationRequestDto fcmNotificationRequestDto) {
        return Notification
                .builder()
                .body(fcmNotificationRequestDto.getBody())
                .notificationType(NotificationType.TEST)
                .userId(user.getId())
                .title(fcmNotificationRequestDto.getTitle())
                .build();
    }
}
