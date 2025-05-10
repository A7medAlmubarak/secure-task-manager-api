package com.example.task.infrastructure.config;

import com.example.task.infrastructure.logging.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggingConfig implements WebMvcConfigurer {
    
    private final LoggingInterceptor loggingInterceptor;

    public LoggingConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
               .addPathPatterns("/**")
               .excludePathPatterns("/health", "/metrics", "/actuator/**");
    }
} 