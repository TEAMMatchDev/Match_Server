package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import lombok.Getter;


@Getter
public class BadRequestException extends BaseException {

    public BadRequestException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}