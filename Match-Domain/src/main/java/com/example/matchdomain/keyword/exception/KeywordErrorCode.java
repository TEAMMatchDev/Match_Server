package com.example.matchdomain.keyword.exception;

import static org.springframework.http.HttpStatus.*;

import java.lang.reflect.Field;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeywordErrorCode implements BaseErrorCode {
    @ExplainError("해당 키워드가 존재하지 않습니다.")
    KEYWORD_ERROR_CODE(BAD_REQUEST,"KEYWORD001", "해당 키워드가 존재하지 않습니다.");


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