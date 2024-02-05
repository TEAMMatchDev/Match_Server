package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.BaseErrorCode;

public class IllegalArgumentException extends BaseException{
	public IllegalArgumentException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
