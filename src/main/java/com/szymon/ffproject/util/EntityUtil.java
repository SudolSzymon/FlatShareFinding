package com.szymon.ffproject.util;

import com.szymon.ffproject.util.annotation.Unmodifiable;
import java.lang.reflect.Field;

public class EntityUtil {

    public static <T> void update(T target, T source) {
        try {
            for (Field field : target.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Unmodifiable.class) || field.get(target) == null) {
                    field.set(target, field.get(source));
                }
            }
        } catch (IllegalAccessException ignored) {

        }
    }
}
