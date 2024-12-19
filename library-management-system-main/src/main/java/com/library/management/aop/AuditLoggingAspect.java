package com.library.management.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggingAspect.class);

    @Pointcut("@annotation(com.example.librarymanagementsystem.aop.AuditLog)")
    public void auditLogMethods() {
        // Pointcut for methods annotated with @AuditLog
    }

    @Before("auditLogMethods()")
    public void logBefore(JoinPoint joinPoint) {
        // This method is executed before the method annotated with @AuditLog
        logger.info("Audit Log - Before execution: Method: {} Arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "auditLogMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // This method is executed after the method annotated with @AuditLog
        logger.info("Audit Log - After execution: Method: {} Result: {}",
                joinPoint.getSignature().getName(),
                result);
    }
}
