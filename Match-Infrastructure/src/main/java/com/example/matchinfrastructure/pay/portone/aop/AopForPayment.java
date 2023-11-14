package com.example.matchinfrastructure.pay.portone.aop;

import com.example.matchcommon.annotation.PaymentIntercept;
import com.example.matchcommon.aop.KeyGenerator;
import com.example.matchinfrastructure.pay.portone.service.PortOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class AopForPayment {
    private final PortOneService portOneService;
    private final KeyGenerator keyGenerator;


    // @PaymentValidator 어노테이션이 붙은 메소드에서 예외가 발생하면 이 메소드가 호출됩니다.
    @AfterThrowing(pointcut = "execution(* *(..)) && @annotation(paymentIntercept)", throwing = "exception")
    public void refundOnPaymentFailure(JoinPoint joinPoint, PaymentIntercept paymentIntercept, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String impUid = (String) keyGenerator.getDynamicValue(methodSignature.getParameterNames(),  joinPoint.getArgs(), paymentIntercept.key());

        log.info("ERROR OCCUR : " + impUid);
        try {
            portOneService.refundPayment(impUid);
            log.error("에러 발생 환불 IMP_UID : " + impUid);
        } catch (Exception e) {
            log.error("환불 처리 중 에러 발생 IMP_UID : " + impUid);
        }
    }

}
