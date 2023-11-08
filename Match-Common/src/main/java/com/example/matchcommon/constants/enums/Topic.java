package com.example.matchcommon.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topic {
    PROJECT_UPLOAD("PROJECT_UPLOAD"), EVENT_UPLOAD("EVENT_UPLOAD");
    private final String topic;
}
