package com.example.matchinfrastructure.aligo.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchcommon.properties.AligoProperties;
import com.example.matchinfrastructure.aligo.dto.AlimTalkReq;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchinfrastructure.aligo.AligoStatic.*;

@Converter
public class AligoConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    public AlimTalkReq.Button createButton(AlimType alimType, Long regularId) {
        return AlimTalkReq.Button
                .builder()
                .name(alimType.equals(AlimType.PAYMENT) ? PAYMENT_BUTTON_NAME : EXECUTION_BUTTON_NAME)
                .linkType(APP_LINK)
                .linkTypeName(APP_LINK_NAME)
                .linkIos(IOS_DEEP_LINK + regularId)
                .linkAnd(AOS_DEEP_LINK + regularId)
                .build();
    }

    private String createButtonJson(AlimType alimType, Long regularId) {
        List<AlimTalkReq.Button> buttons = new ArrayList<>();
        buttons.add(createButton(alimType, regularId));

        try {
            return mapper.writeValueAsString(buttons);
        } catch (Exception e) {
            throw new RuntimeException("Error creating JSON for buttons", e);
        }
    }

    private AlimTalkReq createAlimTalkReq(AligoProperties aligoProperties, String token, AlimType alimType, String phone, String name, String tplCode, String emTitle, String subject, String message, String buttonJson) {
        return AlimTalkReq
                .builder()
                .apikey(aligoProperties.getKey())
                .userid(aligoProperties.getUsername())
                .senderkey(aligoProperties.getSenderKey())
                .tpl_code(tplCode)
                .sender(aligoProperties.getSender())
                .emtitle_1(emTitle)
                .receiver_1(phone)
                .subject_1(subject)
                .message_1(message)
                .token(token)
                .button_1(String.format(BUTTON, buttonJson))
                .build();
    }

    public AlimTalkReq convertToAlimTalkTest(String phone, String name, AligoProperties aligoProperties, String token, AlimType alimType) {
        String json = createButtonJson(alimType, 1L);
        String tplCode = alimType.equals(AlimType.PAYMENT) ? PAYMENT_TEMPLATE : EXECUTION_TEMPLATE;
        String emTitle = alimType.equals(AlimType.PAYMENT) ? PAYMENT_EM_TITLE : EXECUTION_EM_TITLE;
        String subject = alimType.equals(AlimType.PAYMENT) ? PAYMENT_SUBJECT : EXECUTION_SUBJECT;
        String message = alimType.equals(AlimType.PAYMENT) ? String.format(PAYMENT_MESSAGE, name, name) : String.format(EXECUTION_MESSAGE, name, "", "");

        return createAlimTalkReq(aligoProperties, token, alimType, phone, name, tplCode, emTitle, subject, message, json);
    }

    public AlimTalkReq convertToAlimTalk(AligoProperties aligoProperties, String token, AlimType alimType, AlimTalkDto alimTalkDto) {
        String json = createButtonJson(alimType, alimTalkDto.getDonationId());
        String tplCode = alimType.equals(AlimType.PAYMENT) ? PAYMENT_TEMPLATE : EXECUTION_TEMPLATE;
        String emTitle = alimType.equals(AlimType.PAYMENT) ? PAYMENT_EM_TITLE : EXECUTION_EM_TITLE;
        String subject = alimType.equals(AlimType.PAYMENT) ? PAYMENT_SUBJECT : EXECUTION_SUBJECT;
        String message = alimType.equals(AlimType.PAYMENT) ? String.format(PAYMENT_MESSAGE, alimTalkDto.getName(), alimTalkDto.getName()) : String.format(EXECUTION_MESSAGE, alimTalkDto.getName(), alimTalkDto.getArticle(), alimTalkDto.getUsages());

        return createAlimTalkReq(aligoProperties, token, alimType, alimTalkDto.getPhone(), alimTalkDto.getName(), tplCode, emTitle, subject, message, json);
    }

    public AlimTalkDto convertToAlimTalkPayment(Long donationId, String name, String phoneNumber) {
        return AlimTalkDto
                .builder()
                .donationId(donationId)
                .name(name)
                .phone(phoneNumber)
                .build();

    }

    public AlimTalkDto convertToAlimTalkExecution(Long donationId, String name, String phoneNumber, String article, String usages){
        return AlimTalkDto
                .builder()
                .donationId(donationId)
                .name(name)
                .phone(phoneNumber)
                .article(article)
                .usages(usages)
                .build();
    }
}
