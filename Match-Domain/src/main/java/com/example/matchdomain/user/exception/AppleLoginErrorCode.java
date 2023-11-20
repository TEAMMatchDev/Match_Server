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
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum AppleLoginErrorCode implements BaseErrorCode {
    NOT_EXISTS_APPLE_USER(NOT_FOUND,"APPLE_SIGN_UP","해당 애플 로그인 유저의 로그인 정보가 존재하지 않습니다. 반환 필드 중 result 에 있는 값을 가지고 회원가입 진행해주세요");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        HashMap<String,String> data = new HashMap<>();
        data.put("socialId","daoiisjo4e21424.412j4io1241.b231");
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
