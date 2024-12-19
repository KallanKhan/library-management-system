package com.library.management.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
    @AfterReturning(value = "execution(* com.example..*Service.borrowBook(..))", returning = "result")
    public void logBorrowAction(JoinPoint joinPoint, Object result) {
        // Log the borrowing action
    }
}
