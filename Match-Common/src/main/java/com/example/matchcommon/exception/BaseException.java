package com.example.matchcommon.exception;

import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private BaseErrorCode errorCode;

    public BaseException(HttpStatus httpStatus, boolean b, String resultCode, String resultMsg) {
        this.errorCode = new BaseErrorCode() {
            @Override
            public ErrorReason getErrorReason() {
                return ErrorReason.builder().message(resultMsg).code(resultCode).isSuccess(b).build();
            }

            @Override
            public String getExplainError() throws NoSuchFieldException {
                return null;
            }

            @Override
            public ErrorReason getErrorReasonHttpStatus() {
                return ErrorReason.builder().message(resultMsg).code(resultCode).isSuccess(b).httpStatus(httpStatus).build();
            }
        };
    }

    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }

    public ErrorReason getErrorReasonHttpStatus(){
        return this.errorCode.getErrorReasonHttpStatus();
    }

}
