package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.Getter;

@Getter
public class OtherServerException extends BaseException{
    public OtherServerException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
