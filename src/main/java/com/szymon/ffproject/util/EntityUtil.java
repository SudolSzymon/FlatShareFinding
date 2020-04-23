package com.szymon.ffproject.util;

import com.szymon.ffproject.database.entity.Entity;
import com.szymon.ffproject.util.annotation.Unmodifiable;
import java.lang.reflect.Field;
import java.util.Objects;

public class EntityUtil {

    public static <T extends Entity> void update(T target, T source) {
        Objects.requireNonNull(target, "No target object specified");
        Objects.requireNonNull(source, "No source object specified");
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
