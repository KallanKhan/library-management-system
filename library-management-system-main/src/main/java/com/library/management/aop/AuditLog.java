package com.library.management.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    // You can define additional fields in the annotation if needed.
    String action() default ""; // Optional field to specify the type of action (create, update, etc.)
}
