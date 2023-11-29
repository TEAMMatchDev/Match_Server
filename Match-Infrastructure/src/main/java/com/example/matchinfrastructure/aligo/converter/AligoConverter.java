package com.example.matchinfrastructure.aligo.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchcommon.properties.AligoProperties;
import com.example.matchinfrastructure.aligo.dto.AlimTalkReq;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchinfrastructure.aligo.AligoStatic.*;

@Converter
public class AligoConverter {
    public AlimTalkReq.Button convertToButton(AlimType alimType) {
        if (alimType.equals(AlimType.PAYMENT)){
            return AlimTalkReq.Button
                    .builder()
                    .name(PAYMENT_BUTTON_NAME)
                    .linkType(APP_LINK)
                    .linkTypeName(APP_LINK_NAME)
                    .linkIos(IOS_DEEP_LINK)
                    .linkAnd(AOS_DEEP_LINK)
                    .build();
        }
        else{
            return AlimTalkReq.Button
                    .builder()
                    .name(EXECUTION_BUTTON_NAME)
                    .linkType(APP_LINK)
                    .linkTypeName(APP_LINK_NAME)
                    .linkIos(IOS_DEEP_LINK)
                    .linkAnd(AOS_DEEP_LINK)
                    .build();
        }
    }

    public AlimTalkReq convertToPayment(String phone, String name, AligoProperties aligoProperties, String token, AlimType alimType) {
        List<AlimTalkReq.Button> buttons = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;


        if(alimType.equals(AlimType.PAYMENT)){
            buttons.add(convertToButton(alimType));

            try {
                json = mapper.writeValueAsString(buttons);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return AlimTalkReq
                    .builder()
                    .apikey(aligoProperties.getKey())
                    .userid(aligoProperties.getUsername())
                    .senderkey(aligoProperties.getSenderKey())
                    .tpl_code(PAYMENT_TEMPLATE)
                    .sender(aligoProperties.getSender())
                    .emtitle_1(PAYMENT_EM_TITLE)
                    .receiver_1(phone)
                    .subject_1(PAYMENT_SUBJECT)
                    .message_1(String.format(PAYMENT_MESSAGE, name))
                    .token(token)
                    .button_1(String.format(BUTTON, json))
                    .build();
        }
        else{
            buttons.add(convertToButton(alimType));

            try {
                json = mapper.writeValueAsString(buttons);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return AlimTalkReq
                    .builder()
                    .apikey(aligoProperties.getKey())
                    .userid(aligoProperties.getUsername())
                    .senderkey(aligoProperties.getSenderKey())
                    .tpl_code(EXECUTION_TEMPLATE)
                    .sender(aligoProperties.getSender())
                    .emtitle_1(EXECUTION_EM_TITLE)
                    .receiver_1(phone)
                    .subject_1(EXECUTION_SUBJECT)
                    .message_1(String.format(EXECUTION_MESSAGE, name, "", ""))
                    .token(token)
                    .button_1(String.format(BUTTON, json))
                    .build();
        }
    }
}
