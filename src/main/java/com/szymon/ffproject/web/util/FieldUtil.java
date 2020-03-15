package com.szymon.ffproject.web.util;

import com.szymon.ffproject.web.util.annotation.DisplayAs;
import com.szymon.ffproject.web.util.annotation.InputType;
import com.szymon.ffproject.web.util.annotation.Private;
import com.szymon.ffproject.web.util.annotation.Transient;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.ui.Model;

public class FieldUtil {

    public final String name;
    public final String type;
    public final Set<String> options;
    public final String display;

    public FieldUtil(String name, String fieldType, Set<String> options, String display) {
        this.name = name;
        this.type = fieldType;
        this.options = options;
        this.display = display;
    }

    private static List<FieldUtil> getInputList(Object o, Map<String, Set<String>> optionsMap) {
        LinkedList<FieldUtil> list = new LinkedList<>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldType = "text";
            Set<String> fieldOptions = new HashSet<>();
            String fieldDisplay = "";
            if (field.isAnnotationPresent(InputType.class))
                fieldType = field.getAnnotation(InputType.class).type();
            if (field.isAnnotationPresent(DisplayAs.class))
                fieldDisplay = field.getAnnotation(DisplayAs.class).display();

            if (fieldType.equals("valueSelect")) {
                fieldOptions = optionsMap.getOrDefault(field.getName(), Collections.emptySet());
            }
            if (!field.isAnnotationPresent(Transient.class))
                list.add(new FieldUtil(field.getName(), fieldType, fieldOptions,
                                       fieldDisplay.isEmpty() ? field.getName() : fieldDisplay));
        }
        return list;
    }

    public static void addObject(Model model, Object obj, String formUrl, String name,
                                 Map<String, Set<String>> optionsMap) {
        model.addAttribute("formUrl", formUrl);
        model.addAttribute("object", obj);
        model.addAttribute("name", name);
        model.addAttribute("inputs", getInputList(obj, optionsMap == null ? new HashMap<>() : optionsMap));
    }

    public static void addObject(Model model, Object obj, String formUrl, String formName) {
        addObject(model, obj, formUrl, formName, Collections.emptyMap());
    }

    public static void addList(Model model, Iterable<?> list, String listName, String delUrl, String editUrl) {
        addList(model, list, listName, delUrl, editUrl, null);
    }

    public static void addList(
        Model model, Iterable<?> list, String listName, String delUrl, String editUrl, Object viewUrl) {
        model.addAttribute("list", list);
        model.addAttribute("listName", listName);
        if (delUrl != null)
            model.addAttribute("delUrl", delUrl);
        if (editUrl != null)
            model.addAttribute("editUrl", editUrl);
        if (viewUrl != null)
            model.addAttribute("viewUrl", viewUrl);
        if (list.iterator().hasNext())
            model.addAttribute("mappings", getColumnList(list.iterator().next()));
    }

    private static Map<String, String> getColumnList(Object obj) {

        Map<String, String> mapping = new LinkedHashMap<>();
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Private.class))
                if (field.isAnnotationPresent(DisplayAs.class))
                    mapping.put(field.getName(), field.getAnnotation(DisplayAs.class).display());
                else
                    mapping.put(field.getName(), field.getName());
        }
        return mapping;
    }
}
