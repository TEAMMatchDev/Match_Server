package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import lombok.Getter;

import static com.example.matchcommon.exception.errorcode.CommonResponseStatus._UNAUTHORIZED;


@Getter
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
