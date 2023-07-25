package com.example.matchcommon.exception;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException  {

    public NotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
