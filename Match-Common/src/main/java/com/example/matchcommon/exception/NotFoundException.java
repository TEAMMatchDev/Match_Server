package com.example.matchcommon.exception;
import com.example.matchcommon.exception.error.CommonResponseStatus;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException  {
    private String message;

    public NotFoundException(String message) {
        super(CommonResponseStatus._BAD_REQUEST);
        this.message = message;
    }

    public NotFoundException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public NotFoundException(CommonResponseStatus errorCode) {
        super(errorCode);
    }
}
