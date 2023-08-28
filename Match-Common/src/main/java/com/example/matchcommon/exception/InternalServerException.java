package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import lombok.Getter;

@Getter
public class InternalServerException extends BaseException {
    public InternalServerException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
