package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerExpiredTokenException extends BaseException {

    public OtherServerExpiredTokenException(CommonResponseStatus commonResponseStatus) {
        super(commonResponseStatus);
    }
}
