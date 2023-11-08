package com.example.matchinfrastructure.fcm.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;

import static com.example.matchcommon.constants.MatchAlertStatic.PAY_ALERT_BODY;
import static com.example.matchcommon.constants.MatchAlertStatic.ALERT_TITLE;

@Converter
public class FcmConverter {

    public FCMNotificationRequestDto convertToDto(String inherenceName) {
        return FCMNotificationRequestDto
                .builder()
                .title(ALERT_TITLE)
                .body(inherenceName + PAY_ALERT_BODY)
                .build();
    }
}
