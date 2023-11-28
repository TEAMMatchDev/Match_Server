package com.example.matchinfrastructure.aligo.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchcommon.properties.AligoProperties;
import com.example.matchinfrastructure.aligo.dto.AlimTalkReq;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchinfrastructure.aligo.AligoStatic.PAYMENT_TEMPLATE;

@Converter
public class AligoConverter {
    public AlimTalkReq.Button convertToButton() {
        return AlimTalkReq.Button
                .builder()
                .name("내 불꽃이 보러 가기")
                .linkType("AL")
                .linkTypeName("앱링크")
                .linkIos("https://www.official-match.kr/")
                .linkAnd("https://www.official-match.kr/")
                .build();
    }

    public AlimTalkReq convertToPayment(String phone, String name, AligoProperties aligoProperties, String token) {
        List<AlimTalkReq.Button> buttons = new ArrayList<>();

        String message = name + "님의 마음 속 따뜻함이\n" +
                "세상을 따뜻하게 할 불꽃이가 되었습니다.\n" +
                "\n" +
                "어떤 불꽃이가 탄생했는지 \n" +
                "앱에서 확인해보세요  (/^^)/\n";

        ObjectMapper mapper = new ObjectMapper();

        buttons.add(convertToButton());

        String json = null;
        try {
            // Convert the button object to JSON string
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
                .emtitle_1("기부가 시작되었습니다")
                .receiver_1(phone)
                .subject_1("결제 알림")
                .message_1(message)
                .token(token)
                .button_1("{\"button\": " + json + "}")
                .build();
    }
}
