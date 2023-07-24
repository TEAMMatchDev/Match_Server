package com.example.matchcommon.exception;

import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.error.BaseErrorCode;
import com.example.matchcommon.exception.error.CommonResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;


@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private BaseErrorCode errorCode;

    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }

}
