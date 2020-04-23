package com.szymon.ffproject.util;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.szymon.ffproject.database.entity.Entity;
import com.szymon.ffproject.util.annotation.Unmodifiable;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class EntityUtilTest {

    @Test
    void update() {
        TestEntity source = new TestEntity("a", "b", "c");
        TestEntity target = new TestEntity("x", "y", null);
        EntityUtil.update(target, source);

        assertEquals(target.fieldA, "a");
        assertEquals(target.fieldB, "y");
        assertEquals(target.fieldC, "c");
    }

    private static class TestEntity implements Entity {

        @Unmodifiable
        public String fieldA;
        public String fieldC;
        public String fieldB;

        public TestEntity(String a, String b, String c) {
            fieldA = a;
            fieldB = b;
            fieldC = c;
        }

        @Override
        public String getEntityID() {
            return "ID";
        }

        @Override
        public LocalDateTime getCreationTime() {
            return LocalDateTime.now();
        }

        @Override
        public LocalDateTime getModificationTime() {
            return LocalDateTime.now();
        }

        @Override
        public void setModificationTime(LocalDateTime modificationTime) {

        }
    }
}
