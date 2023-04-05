package com.unibuc.main.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.unibuc.main.service.*.*(..))")
    public void logServicesMethod(){
    }

    @Pointcut("execution(* com.unibuc.main.controller.*.*(..))")
    public void logControllerMethod(){
    }

    @Pointcut("@annotation(Log)")
    public void logMethod(){
    }

    @Before("logServicesMethod()")
    public void logBeforeServicesAdvice(JoinPoint joinPoint) {
        log.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());
    }

    @After("logServicesMethod()")
    public void logAfterServicesAdvice(JoinPoint joinPoint) {
        log.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " finished the execution. Timestamp: " + LocalDateTime.now());
    }

    @Around("logControllerMethod()")
    public Object logControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());

        Object val = joinPoint.proceed();

        log.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " finished the execution. Timestamp: " + LocalDateTime.now());

        return val;
    }

    @After("logMethod()")
    public void logMethodAdvice(JoinPoint joinPoint) {
        log.info("Method " + joinPoint.getSignature().getName() +
                " from repository finished the execution. Timestamp: " + LocalDateTime.now());
    }
}
