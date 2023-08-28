package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import lombok.Getter;

import static com.example.matchcommon.exception.errorcode.CommonResponseStatus._BAD_REQUEST;


@Getter
public class ForbiddenException extends BaseException {

    public ForbiddenException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
