package com.example.matchapi.common.aop;

import com.example.matchcommon.reponse.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.example.matchcommon.constants.MatchStatic.ignoredMethods;

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
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = getMethodName(method);

        if(!ignoredMethods.contains(methodName)) {
            log.info("==========================LOG_START==========================");

            log.info("logging start method = {}", methodName);

            String[] parameterNames = methodSignature.getParameterNames();

            Object[] args = joinPoint.getArgs();
            int index = 0;
            for (Object arg : args) {
                if (arg != null) {
                    log.info("parameterNames = {} type = {}, value = {}", parameterNames[index], arg.getClass().getSimpleName(), arg.toString());
                }
                index += 1;
            }
        }
    }

    private String getMethodName(Method method) {
        return method.getName();
    }

    @After("controller()")
    public void afterLogic(JoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);

        String methodName = getMethodName(method);

        if(!ignoredMethods.contains(methodName)) {

            log.info("logging finish method = {}", methodName);

            log.info("==========================LOG_FINISH==========================");
        }
    }

    @AfterReturning(value = "controller()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);

        String methodName = getMethodName(method);

        if(!ignoredMethods.contains(methodName)) {
            if (returnObj != null) {
                log.info("========================RETURN_LOG============================");
                log.info("method name = {}", methodName);
                log.info("return type = {}", returnObj.getClass().getSimpleName());
                log.info("return value = {}", returnObj.toString());
                log.info("==============================================================");
            }
        }
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
