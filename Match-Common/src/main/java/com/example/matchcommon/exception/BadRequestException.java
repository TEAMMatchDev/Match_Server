package com.example.matchcommon.exception;

import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.error.CommonResponseStatus;
import com.example.matchcommon.exception.error.ProjectErrorCode;
import lombok.Getter;

import static com.example.matchcommon.exception.error.CommonResponseStatus._BAD_REQUEST;


@Getter
public class BadRequestException extends BaseException {
    private String message;

    public BadRequestException(CommonResponseStatus errorCode) {
        super(errorCode);
    }

}