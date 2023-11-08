package com.example.matchbatch.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.notification.entity.Notification;
import com.example.matchdomain.notification.enums.NotificationType;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;

@Converter
public class NotificationConverter {

    public Notification convertToNotification(FCMNotificationRequestDto fcmNotificationRequestDto, DonationUser donationUser) {
        return Notification
                .builder()
                .userId(donationUser.getUserId())
                .donationUser(donationUser)
                .notificationType(NotificationType.DONATION)
                .title(fcmNotificationRequestDto.getTitle())
                .body(fcmNotificationRequestDto.getBody())
                .build();
    }
}
