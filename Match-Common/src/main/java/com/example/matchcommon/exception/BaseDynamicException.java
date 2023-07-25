package com.example.matchcommon.exception;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.example.matchcommon.exception.errorcode.CommonResponseStatus;
import com.example.matchcommon.exception.errorcode.UserSignUpErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class BaseDynamicException extends RuntimeException {
    UserSignUpErrorCode status;
    Map<String,String> data;

    public BaseDynamicException(UserSignUpErrorCode errorReason, HashMap<String, String> data) {
        this.status = errorReason;
        this.data = data;
    }


}
