package com.szymon.ffproject.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long execStart = System.currentTimeMillis();
        request.setAttribute("execStart", execStart);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {
        long renderStart = System.currentTimeMillis();
        request.setAttribute("renderStart", renderStart);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) {
        long renderStart = (Long) request.getAttribute("renderStart");
        long execStart = (Long) request.getAttribute("execStart");
        long execTime = renderStart - execStart;
        long renderingDuration = System.currentTimeMillis() - renderStart;
        String path = request.getServletPath();
        String username = request.getUserPrincipal().getName();
        logger.info(
            String.format("[REQUEST METADATA] User: %s, Path: %s, Executed in: %s, Rendered in: %s", username, path, execTime,
                          renderingDuration));
    }
}
