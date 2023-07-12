package com.example.matchcommon.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException {

    HttpStatus httpStatus;
    CommonResponseStatus status;
    String responseMessage;
    Object result;
    Map<String, String> data;


    public BaseException(CommonResponseStatus status) {
        super();
        this.status = status;
        this.responseMessage = status.getMessage();
        this.httpStatus=status.getHttpStatus();
    }

    public BaseException(CommonResponseStatus status, Map<String, String> data) {
        super();
        this.status = status;
        this.responseMessage = status.getMessage();
        this.httpStatus=status.getHttpStatus();
        this.data = data;
    }



}
