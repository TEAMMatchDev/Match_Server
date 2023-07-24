package com.example.matchcommon.exception.error;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * 에러 코드 관리
 */
@Getter
@AllArgsConstructor
public enum CommonResponseStatus implements BaseErrorCode {

    /**
     * 잘못된 요청
     */
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON000", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST,"COMMON001","잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED,"COMMON002","권한이 잘못되었습니다"),
    _METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "COMMON003", "지원하지 않는 Http Method 입니다."),

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
    INVALID_REFRESH_TOKEN(BAD_REQUEST,"AUTH009","리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
    @ExplainError("토큰을 입력 안했을 경우")
    NOT_EMPTY_TOKEN(BAD_REQUEST,"AUTH010","토큰이 비어있습니다 토큰을 보내주세요"),
    @ExplainError("토큰에 담긴 유저가 없는 경우")
    NOT_EXISTS_USER_HAVE_TOKEN(BAD_REQUEST,"AUTH011", "해당 토큰을 가진 유저가 존재하지 않습니다."),






    /*
     * 1000 : 소셜 관련 예외 처리
     */



    APPLE_BAD_REQUEST(BAD_REQUEST, "OAUTH001", "애플 토큰이 잘못되었습니다."),
    APPLE_SERVER_ERROR(FORBIDDEN, "OAUTH002", "애플 서버와 통신에 실패 하였습니다."),
    FAIL_TO_MAKE_APPLE_PUBLIC_KEY(BAD_REQUEST, "OAUTH003", "새로운 애플 공개키 생성에 실패하였습니다."),







    /**
     * UXXX : USER 관련 에러
     */
    //
    USER_STATUS_UNACTIVATED(BAD_REQUEST,"U000","유저가 비활성화 상태입니다."),
    USERS_EMPTY_USER_ID(BAD_REQUEST, "U001", "유저 아이디 값을 입력해주세요."),
    NOT_CORRECT_PASSWORD(BAD_REQUEST, "U002", "유저 비밀번호를 확인해주세요."),
    TOO_SHORT_PASSWORD(BAD_REQUEST, "U003", "비밀번호의 길이를 8자 이상을 설정해주세요."),

    FAILED_TO_SIGN_UP(FORBIDDEN, "U004", "회원가입에 실패하였습니다."),

    USERS_EXISTS_PHONE(FORBIDDEN,"U005","중복된 전화번호입니다."),

    USERS_EXISTS_EMAIL(FORBIDDEN,"U007","중복된 이메일입니다."),


    FAILED_TO_LOGIN(BAD_REQUEST, "U008", "로그인에 실패하였습니다."),
    NOT_EXIST_USER(BAD_REQUEST,"U009" , "해당 유저가 존재하지 않습니다."),
    EXIST_USER_PHONENUMBER(BAD_REQUEST,"U010","이미 해당 유저가 다른 회원가입 방식으로 가입 하였습니다 result 를 확인해주세요."),
    OTHER_SERVER_BAD_REQUEST(BAD_REQUEST, "FEIGN_400_1", "Other server bad request"),
    OTHER_SERVER_UNAUTHORIZED(BAD_REQUEST, "FEIGN_400_2", "Other server unauthorized"),
    OTHER_SERVER_FORBIDDEN(BAD_REQUEST, "FEIGN_400_3", "Other server forbidden"),
    OTHER_SERVER_EXPIRED_TOKEN(BAD_REQUEST, "FEIGN_400_4", "Other server expired token"),
    OTHER_SERVER_NOT_FOUND(BAD_REQUEST, "FEIGN_400_5", "Other server not found error"),
    OTHER_SERVER_INTERNAL_SERVER_ERROR(BAD_REQUEST, "FEIGN_400_6", "Other server internal server error");























    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder().message(message).code(code).status(httpStatus).build();
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getMessage();
    }
}
