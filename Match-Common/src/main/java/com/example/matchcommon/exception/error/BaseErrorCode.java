package com.example.matchcommon.exception.error;

import com.example.matchcommon.dto.ErrorReason;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();

    String getExplainError() throws NoSuchFieldException;

}
