package com.example.matchapi.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.example.matchapi..*Controller.*(..))")
    public void controller() {
    }


    @Pointcut("execution(* com.example.matchapi..*Service.*(..))")
    public void service() {
    }


    @Before("controller()")
    public void beforeLogic(JoinPoint joinPoint) throws Throwable {
        log.info("==========================LOG_START==========================");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("logging start method = {}", method.getName());

        String[] parameterNames = methodSignature.getParameterNames();

        Object[] args = joinPoint.getArgs();
        int index = 0;
        for (Object arg : args) {
            if(arg != null) {
                log.info("parameterNames = {} type = {}, value = {}", parameterNames[index], arg.getClass().getSimpleName(), arg.toString());
                index += 1;
            }

        }
    }

    @After("controller()")
    public void afterLogic(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("logging finish method = {}", method.getName());

        log.info("==========================LOG_FINISH==========================");
    }

}
