package com.example.matchdomain.donation.exception;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum DonationRefundErrorCode implements BaseErrorCode {
    @ExplainError("해당 도네이션이 존재하지 않습니다.")
    DONATION_NOT_EXIST(NOT_FOUND,"DONATION001", "도네이션 ID 값이 존재하지 않습니다."),
    @ExplainError("도네이션 환불 권한이 없습니다.")
    DONATION_NOT_CORRECT_USER(BAD_REQUEST,"DONATION002","기부 환불할 권한이 없습니다. 삭제권한이 없습니다."),
    @ExplainError("집행 전인 기부금만 환불받을 수 있습니다.")
    CANNOT_DELETE_DONATION_STATUS(BAD_REQUEST,"DONATION003", "이미 해당 기부금이 집행중이거나, 집행완료 되었습니다."),;


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