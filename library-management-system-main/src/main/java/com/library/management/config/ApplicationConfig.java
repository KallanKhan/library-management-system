package com.library.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class ApplicationConfig {
    // No need to add beans, @EnableAspectJAutoProxy will automatically detect and apply aspects
}