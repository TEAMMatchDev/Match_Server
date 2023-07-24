package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;
import lombok.Getter;

import static com.example.matchcommon.exception.error.CommonResponseStatus._BAD_REQUEST;


@Getter
public class ForbiddenException extends BaseException {
    private String message;

    public ForbiddenException(String message) {
        super(_BAD_REQUEST);
        this.message = message;
    }

    public ForbiddenException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public ForbiddenException(CommonResponseStatus errorCode) {
        super(errorCode);
    }

}
