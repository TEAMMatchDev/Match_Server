package com.example.matchapi.notification.service;

import com.example.matchapi.notification.converter.NotificationConverter;
import com.example.matchapi.notification.dto.NotificationRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notification.adaptor.NotificationAdaptor;
import com.example.matchdomain.notification.entity.Notification;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationAdaptor notificationAdaptor;
    private final NotificationConverter notificationConverter;

    public PageResponse<NotificationRes.NotificationListInfo> getNotificationList(User user, int page, int size) {
        Page<Notification> notifications = notificationAdaptor.findByUser(user, page, size);
        int notificationCount = notificationAdaptor.countByUnRead(user);
        return new PageResponse<>(notifications.isLast(), notifications.getTotalElements(), new NotificationRes.NotificationListInfo(notificationCount, notificationConverter.convertToNotificationList(notifications.getContent())));
    }

    public NotificationRes.NotificationDetail getNotificationDetail(Long notificationId) {
        Notification notification = notificationAdaptor.findNotification(notificationId);
        notificationAdaptor.readNotification(notification);
        return notificationConverter.convertNotificationDetail(notification);
    }
}
