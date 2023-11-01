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
public enum PatchProjectImageErrorCode implements BaseErrorCode {

    @ExplainError("해당 프로젝트가 존재하지 않습니다.")
    PROJECT_NOT_EXIST(HttpStatus.BAD_REQUEST,false,"PROJECT_IMG001","해당 프로젝트가 존재하지 않습니다."),
    @ExplainError("해당 프로젝트 이미지가 아닌 경우 에러 메시지가 나옵니다..")
    PROJECT_NOT_CORRECT_IMAGE(HttpStatus.BAD_REQUEST,false,"PROJECT_IMG002","해당 프로젝트 이미지가 아닙니다."),
    @ExplainError("해당 프로젝트 이미지가 존재하지 않는 경우")
    PROJECT_IMAGE_NOT_EXIST(HttpStatus.NOT_FOUND, false, "PROJECT_IMG003","해당 이미지가 존재하지 않습니다."),
    @ExplainError("이미지를 업로드 해주세요")
    PROJECT_IMG_EMPTY(HttpStatus.BAD_REQUEST,false ,"PROJECT_IMG004","이미지를 업로드 해주세요");

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
