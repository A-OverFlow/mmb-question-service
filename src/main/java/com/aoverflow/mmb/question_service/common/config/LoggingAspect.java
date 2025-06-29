package com.aoverflow.mmb.question_service.common.config;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Around(
        "execution(* *(..)) && (" +
            "@within(org.springframework.web.bind.annotation.RestController) || " +
            "@within(org.springframework.stereotype.Service) || " +
            "@within(org.springframework.stereotype.Repository))"
    )
    @Observed(name = "Aop-Logging", contextualName = "method-logging")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = String.format("%-30s", joinPoint.getSignature().getDeclaringType().getSimpleName());
        String methodName = String.format("%-30s", joinPoint.getSignature().getName());
        log.info("➡️ Entering : $className #$methodName");

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            String duration = String.format("%4d", System.currentTimeMillis() - startTime);
            log.info("✅ Completed: {} #{} in    {} ms", className, methodName, duration);
            return result;
        } catch (Throwable e) {
            String duration = String.format("%4d", System.currentTimeMillis() - startTime);
            log.error("❌ Failed   : {} #{} in    {} ms", className, methodName, duration);
            throw e;
        }
    }
}
