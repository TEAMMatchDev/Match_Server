package com.example.matchcommon.exception;

import com.example.matchcommon.exception.errorcode.ProjectErrorCode;
import lombok.Getter;

@Getter
public class ProjectException extends BaseException {
    public ProjectException(ProjectErrorCode projectErrorCode) {
        super(projectErrorCode);
    }
}
