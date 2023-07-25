package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.CommonResponseStatus;

public class OtherServerNotFoundException extends BaseException {
    public static final BaseException EXCEPTION = new OtherServerNotFoundException();

    private OtherServerNotFoundException() {
        super(CommonResponseStatus.OTHER_SERVER_NOT_FOUND);
    }
}
