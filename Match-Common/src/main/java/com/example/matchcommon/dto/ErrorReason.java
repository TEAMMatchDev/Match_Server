package com.example.matchcommon.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Builder
public class ErrorReason {
    private HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final Map<String, String> data;

    public boolean getIsSuccess(){
        return isSuccess;
    }
}

