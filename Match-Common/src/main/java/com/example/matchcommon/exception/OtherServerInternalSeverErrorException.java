package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.CommonResponseStatus;

public class OtherServerInternalSeverErrorException extends BaseException {

    public static final BaseException EXCEPTION =
            new OtherServerInternalSeverErrorException();

    private OtherServerInternalSeverErrorException() {
        super(CommonResponseStatus.OTHER_SERVER_INTERNAL_SERVER_ERROR);
    }
}
