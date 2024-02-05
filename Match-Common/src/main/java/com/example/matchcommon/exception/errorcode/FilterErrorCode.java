package com.example.matchcommon.exception.errorcode;

import java.lang.reflect.Field;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterErrorCode implements BaseErrorCode{

	/**
	 * 잘못된 요청
	 */
	NOT_EXISTS_FILTER(HttpStatus.BAD_REQUEST, "FILTER_001", "필터값이 존재하지 않습니다.");


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
