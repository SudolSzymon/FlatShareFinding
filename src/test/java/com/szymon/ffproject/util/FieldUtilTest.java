package com.szymon.ffproject.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.collect.Sets;
import com.szymon.ffproject.util.annotation.DisplayAs;
import com.szymon.ffproject.util.annotation.InputType;
import com.szymon.ffproject.util.annotation.Private;
import com.szymon.ffproject.util.annotation.Transient;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@SpringBootTest
class FieldUtilTest {

    @Test
    void addObject() {
        Model model = new ExtendedModelMap();
        TestObject object = new TestObject();
        Map<String, Set<String>> values = new HashMap<>();
        values.put("b", Sets.newHashSet("oA"));
        FieldUtil.addObject(model, object, "form", "name", values);
        assertEquals("form", model.getAttribute("formUrl"));
        assertEquals("name", model.getAttribute("name"));
        assertEquals(object, model.getAttribute("object"));
        assertNotNull(model.getAttribute("inputs"));
    }

    @Test
    void addList() {
        Model model = new ExtendedModelMap();
        List<TestObject> list = Collections.singletonList(new TestObject());
        FieldUtil.addList(model, list, "name", "delurl", "editurl", "viewurl");
        assertEquals("name", model.getAttribute("listName"));
        assertEquals(list, model.getAttribute("list"));
        assertEquals("delurl", model.getAttribute("delUrl"));
        assertEquals("viewurl", model.getAttribute("viewUrl"));
        assertEquals("editurl", model.getAttribute("editUrl"));
        Map<String, String> mappings = (Map<String, String>) model.getAttribute("mappings");
        assertEquals("a", mappings.get("a"));
        assertEquals("testD", mappings.get("b"));
        assertEquals("c", mappings.get("c"));
        assertNull(mappings.get("d"));
    }


    private static class TestObject {

        @InputType(type = "test")
        public String a;
        @DisplayAs(display = "testD")
        @InputType(type = "valueSelect")
        public String b;
        @Transient
        public String c;
        @Private
        @InputType(type = "test")
        public String d;
    }
}
