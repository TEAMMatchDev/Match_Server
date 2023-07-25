package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.CommonResponseStatus;

public class OtherServerExpiredTokenException extends BaseException {
    public OtherServerExpiredTokenException(CommonResponseStatus commonResponseStatus) {
        super(commonResponseStatus);
    }
}
