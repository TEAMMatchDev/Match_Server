package com.example.matchdomain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.matchdomain.common.model.MessageType.*;

@Getter
@AllArgsConstructor
public enum RandomMessage {
    WARM(PAY_SUCCESS, "도움이 필요한 곳에 누구보다 따뜻한 온기를 전할게요."),
    THANK_YOU(PAY_SUCCESS, "태어나게 해줘서 감사해요! 식사 맛있게 드세요 :)"),
    WARM_MIND(PAY_SUCCESS,"기부자님의 따뜻한 마음으로 제가 태어났어요."),

    WAITING(PAY_ONE_DAY, "저는 지금 친구들을 기다리고 있어요"),
    WAITING2(PAY_ONE_DAY, "잠시만 기다려주세요! 불꽃들이 당신을 위해 준비 중입니다. 🔥"),
    WAITING3(PAY_ONE_DAY, "불꽃이 곧 타오르게 됩니다. 기대해주세요! ✨"),
    WAITING4(PAY_ONE_DAY, "잠시만요! 더 멋진 불꽃을 위해 마지막 터치를 하고 있어요. 💥"),
    WAITING5(PAY_ONE_DAY, "당신을 기다리고 있는 불꽃이 더 빛나게 하기 위해 준비 중입니다! 🌟"),

    MOVING(UNDER,"저는 지금 다른 불꽃이 친구들과 함께 후원처로 이동하고있어요"),
    COMPLETED(COMPLETE,"저는 지금 후원처에 전달되었어요");

    private final MessageType messageType;
    private final String message;
}

