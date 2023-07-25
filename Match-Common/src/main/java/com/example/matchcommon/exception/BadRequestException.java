package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import lombok.Getter;


@Getter
public class BadRequestException extends BaseException {
    private String message;

    public BadRequestException(CommonResponseStatus errorCode) {
        super(errorCode);
    }

}