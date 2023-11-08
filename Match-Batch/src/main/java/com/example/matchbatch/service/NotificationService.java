package com.example.matchbatch.service;

import com.example.matchbatch.converter.NotificationConverter;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.notification.adaptor.NotificationAdaptor;
import com.example.matchdomain.user.adaptor.UserFcmAdaptor;
import com.example.matchdomain.user.entity.UserFcmToken;
import com.example.matchinfrastructure.fcm.converter.FcmConverter;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import com.example.matchinfrastructure.fcm.service.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationConverter converter;
    private final NotificationAdaptor notificationAdaptor;
    private final FcmConverter fcmConverter;
    private final UserFcmAdaptor userFcmAdaptor;
    private final FcmNotificationService fcmNotificationService;


    @Async("notification-payment")
    public void sendPaymentSuccess(DonationUser donationUser) {
        List<UserFcmToken> userFcmTokens = userFcmAdaptor.findByUser(donationUser.getUserId());

        FCMNotificationRequestDto fcmNotificationRequestDto = fcmConverter.convertToDto(donationUser.getInherenceName());
        for(UserFcmToken userFcmToken : userFcmTokens) {
            fcmNotificationRequestDto.setToken(userFcmToken.getFcmToken());
            fcmNotificationService.sendNotificationRegularPayments(fcmNotificationRequestDto, fcmConverter.convertToPayData());
        }

        notificationAdaptor.saveNotification(converter.convertToNotification(fcmNotificationRequestDto, donationUser));
    }
}
