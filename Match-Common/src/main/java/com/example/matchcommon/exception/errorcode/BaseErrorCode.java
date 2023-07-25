package com.example.matchcommon.exception.errorcode;

import com.example.matchcommon.dto.ErrorReason;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();

    String getExplainError() throws NoSuchFieldException;

    public ErrorReason getErrorReasonHttpStatus();


}
