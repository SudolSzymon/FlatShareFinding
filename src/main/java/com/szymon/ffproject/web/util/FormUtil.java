package com.szymon.ffproject.web.util;

import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.InputType;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.ui.Model;

public class FormUtil {

    public final String name;
    public final String type;
    public final Set<String> options;

    public FormUtil(String name, String fieldType, Set<String> options) {
        this.name = name;
        this.type = fieldType;
        this.options = options;
    }

    public static List<FormUtil> getInputList(Object o) {
        return getInputList(o, null);
    }

    public static List<FormUtil> getInputList(Object o, Map<String, Set<String>> optionsMap) {
        LinkedList<FormUtil> list = new LinkedList<>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldType = "text";
            Set<String> fieldOptions = new HashSet<>();
            if (field.isAnnotationPresent(InputType.class))
                fieldType = field.getAnnotation(InputType.class).type();
            if (fieldType.equals("valueSelect")) {
                fieldOptions = optionsMap.getOrDefault(field.getName(), Collections.emptySet());
            }
            if (!field.isAnnotationPresent(FormTransient.class))
                list.add(new FormUtil(field.getName(), fieldType, fieldOptions));
        }
        return list;
    }

    public static void addForm(Model model, Object obj, String formUrl, String formName,
                               Map<String, Set<String>> optionsMap) {
        model.addAttribute("formUrl", formUrl);
        model.addAttribute("object", obj);
        model.addAttribute("formName", formName);
        model.addAttribute("inputs", getInputList(obj, optionsMap));
    }

    public static void addForm(Model model, Object obj, String formUrl, String formName) {
        addForm(model, obj, formUrl, formName, null);
    }
}
