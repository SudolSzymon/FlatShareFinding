package com.szymon.ffproject.util;

import org.springframework.context.ApplicationContext;

public class AppContext {

    private static ApplicationContext context;

    public static <T> T getBeanByClass(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static void init(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}
