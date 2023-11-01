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
public enum AppleErrorCode implements BaseErrorCode {
    @ExplainError("공개키 에러 관리자에게 문위")
    MISMATCH_APPLE_KEY(BAD_REQUEST,"APPLE_001","공개 키중 일치하는 키 값이 존재하지 않습니다.");
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
