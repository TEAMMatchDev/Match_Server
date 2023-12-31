package com.example.matchdomain.user.exception;

import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

import com.example.matchcommon.annotation.ExplainError;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum UserAuthErrorCode implements BaseErrorCode {
    /*
       인증 관련 에러코드
    */

    @ExplainError("유저 권한이 없는 경우")
    ForbiddenException(UNAUTHORIZED,"AUTH002", "해당 요청에 대한 권한이 없습니다."),
    @ExplainError("토큰을 입력 안했을 경우")
    UNAUTHORIZED_EXCEPTION (UNAUTHORIZED,"AUTH003", "로그인 후 이용가능합니다. 토큰을 입력해 주세요"),
    @ExplainError("토큰이 만료됐을 경우")
    EXPIRED_JWT_EXCEPTION(UNAUTHORIZED,"AUTH004", "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    @ExplainError("액세스 토큰과 리프레쉬 토큰이 모두 만료됐을 경우")
    RELOGIN_EXCEPTION(UNAUTHORIZED,"AUTH005", "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
    @ExplainError("액세스 토큰과 리프레쉬 토큰이 모두 만료됐을 경우")
    INVALID_TOKEN_EXCEPTION(UNAUTHORIZED,"AUTH006","토큰이 올바르지 않습니다." ),
    @ExplainError("이미 로그아웃 한 리프레쉬 토큰으로 로그아웃 한 경우")
    HIJACK_JWT_TOKEN_EXCEPTION(UNAUTHORIZED,"AUTH007","탈취된(로그아웃 된) 토큰입니다 다시 로그인 해주세요."),
    @ExplainError("리프레쉬 토큰이 만료됐을 경우")
    INVALID_REFRESH_TOKEN(UNAUTHORIZED,"AUTH009","리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
    @ExplainError("토큰을 입력 안했을 경우")
    NOT_EMPTY_TOKEN(UNAUTHORIZED,"AUTH010","토큰이 비어있습니다 토큰을 보내주세요"),
    @ExplainError("토큰에 담긴 유저가 없는 경우")
    NOT_EXISTS_USER_HAVE_TOKEN(UNAUTHORIZED,"AUTH011", "해당 토큰을 가진 유저가 존재하지 않습니다."),
    NOT_USER_ACTIVE(UNAUTHORIZED,"AUTH012","유저가 비활성 상태입니다 로그인 할 수 없습니다."),
    @ExplainError("유저가 존재하지 않는 경우")
    NOT_EXIST_USER(NOT_FOUND,"U009" , "해당 유저가 존재하지 않습니다."),

    NOT_ALLOWED_ACCESS(UNAUTHORIZED,"U010","접근 권한이 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder().message(message).code(code).isSuccess(false).build();
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
