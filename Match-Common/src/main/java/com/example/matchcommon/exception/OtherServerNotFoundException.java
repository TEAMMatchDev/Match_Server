package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerNotFoundException extends BaseException {
    public static final BaseException EXCEPTION = new OtherServerNotFoundException();

    private OtherServerNotFoundException() {
        super(CommonResponseStatus.OTHER_SERVER_NOT_FOUND);
    }
}
