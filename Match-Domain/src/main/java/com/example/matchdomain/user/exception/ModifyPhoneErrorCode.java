package com.example.matchdomain.user.exception;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Getter
@AllArgsConstructor
public enum ModifyPhoneErrorCode implements BaseErrorCode {
    @ExplainError("핸드폰 번호가 일치하지 않습니다.")
    NOT_CORRECT_PHONE(BAD_REQUEST,"U030","핸드폰 번호가 일치하지 않습니다."),
    @ExplainError("유저가 이미 가입된 전화번호로 가입을 요청하는 경우")
    USERS_EXISTS_PHONE(FORBIDDEN,"U005","중복된 전화번호입니다.");
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
