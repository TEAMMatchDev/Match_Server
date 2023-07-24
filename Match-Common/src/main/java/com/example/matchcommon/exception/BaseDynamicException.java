package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class BaseDynamicException extends RuntimeException {
    CommonResponseStatus status;
    Map<String,String> data;

    public BaseDynamicException(CommonResponseStatus errorReason, HashMap<String, String> data) {
        this.status = errorReason;
        this.data = data;
    }
}
