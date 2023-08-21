package com.example.matchdomain.project.exception;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ProjectErrorCode implements BaseErrorCode {

    @ExplainError("해당 프로젝트가 존재하지 않습니다.")
    PROJECT_NOT_EXIST(HttpStatus.BAD_REQUEST,false,"PROJECT001","해당 프로젝트가 존재하지 않습니다."),
    PROJECT_NOT_DONATION_STATUS(HttpStatus.BAD_REQUEST,false, "PROJECT002","해당 프로젝트는 기부 기한이 지났거나 마감되어 진행할 수 없습니다.");
    private final HttpStatus httpStatus;
    private final boolean isSuccess;
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
