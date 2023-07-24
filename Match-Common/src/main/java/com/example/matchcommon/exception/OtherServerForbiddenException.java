package com.example.matchcommon.exception;

import com.example.matchcommon.exception.error.CommonResponseStatus;

public class OtherServerForbiddenException extends BaseException {
    public OtherServerForbiddenException(CommonResponseStatus commonResponseStatus) {
        super(commonResponseStatus);
    }
}
