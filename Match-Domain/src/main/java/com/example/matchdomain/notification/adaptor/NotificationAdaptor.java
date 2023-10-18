package com.example.matchdomain.notification.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class NotificationAdaptor {
    private final NotificationRepository notificationRepository;
}
