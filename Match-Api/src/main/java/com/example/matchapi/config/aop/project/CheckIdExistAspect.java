package com.example.matchapi.config.aop.project;

import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.example.matchdomain.project.exception.ProjectErrorCode.PROJECT_NOT_EXIST;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckIdExistAspect {
    private final ProjectRepository projectRepository;
    @Before("@annotation(com.example.matchapi.config.aop.project.CheckProjectIdExist)")
    public void checkIdsExist(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if ("projectId".equals(parameterNames[i])) {
                Long projectId = (Long) args[i];
                if (!projectRepository.existsById(projectId)) {
                    throw new NotFoundException(PROJECT_NOT_EXIST);
                }
                break;
            }
        }

    }
}
