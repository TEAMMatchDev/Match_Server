package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerUnauthorizedException extends BaseException {

    public static final BaseException EXCEPTION = new OtherServerUnauthorizedException();

    private OtherServerUnauthorizedException() {
        super(CommonResponseStatus.OTHER_SERVER_UNAUTHORIZED);
    }
}
