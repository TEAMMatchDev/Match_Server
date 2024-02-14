package com.example.matchdomain.notice.exception;

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
public enum GetNoticeErrorCode implements BaseErrorCode {

    @ExplainError("해당 공지사항이 존재하지 않습니다.")
    NOT_EXIST_NOTICE(HttpStatus.BAD_REQUEST,false,"NOTICE_001","해당 공지사항이 존재하지 않습니다..")
    ,NOT_EXIST_CONTENT(HttpStatus.BAD_REQUEST,false,"NOTICE_002","해당 공지사항의 컨텐츠가 존재하지 않습니다.");

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
