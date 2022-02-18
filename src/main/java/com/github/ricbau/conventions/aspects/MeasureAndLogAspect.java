package com.github.ricbau.conventions.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MeasureAndLogAspect {

    /*
    Just a simple example of a cross-cutting reusable behaviour.
    In normal unit tests, this would not even be triggered,
    allowing more lightweight tests
     */
    @Around("@annotation(com.github.ricbau.conventions.aspects.MeasureAndLog)")
    public Object measureAndLogTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("Method {} executed in {} millis",
                proceedingJoinPoint.getSignature().getName(),
                executionTime);
        return result;
    }
}
