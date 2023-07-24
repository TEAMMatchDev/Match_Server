package com.example.matchdomain.project.exception;

import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.exception.error.ProjectErrorCode;
import lombok.Getter;

@Getter
public class ProjectException extends BaseException {
    public ProjectException(ProjectErrorCode projectErrorCode) {
        super(projectErrorCode);
    }
}
