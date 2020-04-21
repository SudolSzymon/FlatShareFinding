package com.szymon.ffproject.config;

import com.szymon.ffproject.controller.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    final
    LoggingInterceptor interceptor;

    public MVCConfig(LoggingInterceptor interceptor) {this.interceptor = interceptor;}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
