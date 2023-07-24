package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerUnauthorizedException extends BaseException {
    public OtherServerUnauthorizedException(CommonResponseStatus otherServerUnauthorized) {
        super(CommonResponseStatus.OTHER_SERVER_UNAUTHORIZED);
    }
}
