package com.example.matchdomain.user.exception;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum UserSignUpErrorCode implements BaseErrorCode {
    @ExplainError("유저가 이미 같은 번호로 회원가입을 한 경우")
    EXIST_USER_PHONENUMBER(BAD_REQUEST,"U010","이미 해당 유저가 다른 회원가입 방식으로 가입 하였습니다 result 를 확인해주세요");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        HashMap<String,String> data = new HashMap<>();
        data.put("signUpType","이미 가입된 (가입한 타입) 계정이 존재 합니다. (가입한 타입) 계정으로 로그인 해주세요.");
        return ErrorReason.builder().message(message).code(code).isSuccess(false).result(data).build();
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getMessage();
    }

    @Override
    public ErrorReason getErrorReasonHttpStatus(){
        return ErrorReason.builder().message(message).code(code).isSuccess(false).httpStatus(httpStatus).build();
    }
}
