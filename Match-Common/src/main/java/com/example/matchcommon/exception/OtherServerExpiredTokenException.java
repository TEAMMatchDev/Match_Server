package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerExpiredTokenException extends BaseException {

    public static final BaseException EXCEPTION = new OtherServerExpiredTokenException();

    private OtherServerExpiredTokenException() {
        super(CommonResponseStatus.OTHER_SERVER_EXPIRED_TOKEN);
    }
}
