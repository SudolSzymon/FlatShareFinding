package com.szymon.ffproject.web.util;

import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.InputType;
import com.szymon.ffproject.web.util.annotation.ValueSelectInputType;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.springframework.ui.Model;

public class FormUtil {

    public  final String name;
    public final String type;
    public final String[]  options;

    public FormUtil(String name, String fieldType, String[] options) {
        this.name = name;
        this.type = fieldType;
        this.options = options;
    }

    public  static List<FormUtil> getInputList(Object o) {
        LinkedList<FormUtil> list = new LinkedList<>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldType = "text";
            String[] fieldOptions = new String[0];
            if (field.isAnnotationPresent(InputType.class))
                fieldType = field.getAnnotation(InputType.class).type();
            else if (field.isAnnotationPresent(ValueSelectInputType.class)) {
                fieldType = field.getAnnotation(ValueSelectInputType.class).type();
                fieldOptions = field.getAnnotation(ValueSelectInputType.class).values();
            }
            if (!field.isAnnotationPresent(FormTransient.class))
                list.add(new FormUtil(field.getName(), fieldType, fieldOptions));
        }
        return list;
    }

    public static void addForm(Model model, Object obj, String formUrl, String formName) {
        model.addAttribute("formUrl", formUrl);
        model.addAttribute("object", obj);
        model.addAttribute("formName", formName);
        model.addAttribute("inputs", getInputList(obj));
    }
}
