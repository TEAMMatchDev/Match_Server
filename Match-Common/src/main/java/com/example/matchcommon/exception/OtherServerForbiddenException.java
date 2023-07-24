package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerForbiddenException extends BaseException {

    public static final BaseException EXCEPTION = new OtherServerForbiddenException();

    private OtherServerForbiddenException() {
        super(CommonResponseStatus.OTHER_SERVER_FORBIDDEN);
    }
}
