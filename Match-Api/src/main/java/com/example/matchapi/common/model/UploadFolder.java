package com.example.matchapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadFolder {
    EVENT("event"), NOTICE("notice");

    private final String folder;
}
