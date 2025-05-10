package com.example.task.infrastructure.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper;

    public LoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.example.test.infrastructure.logging.annotation.Loggable)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringType().getSimpleName();

        // Log method entry
        logger.info("→ Entering {}.{}", className, methodName);
        
        // Log parameters if any
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            logger.debug("Method arguments: {}", objectMapper.writeValueAsString(args));
        }

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            
            // Log execution time
            logger.info("← Exiting {}.{} - Execution time: {}ms", 
                className, methodName, (endTime - startTime));
            
            return result;
        } catch (Exception e) {
            logger.error("✖ Exception in {}.{}: {}", 
                className, methodName, e.getMessage(), e);
            throw e;
        }
    }
}