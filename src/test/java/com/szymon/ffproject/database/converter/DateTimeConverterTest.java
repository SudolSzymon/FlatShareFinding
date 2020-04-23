package com.szymon.ffproject.database.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.szymon.ffproject.util.AppContext;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DateTimeConverterTest {

    String now = "2020-04-20T10:30:00";
    LocalDateTime date = LocalDateTime.of(2020, 4, 20, 10, 30, 0);
    DateTimeConverter converter = new DateTimeConverter();
    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void setContext() {
        AppContext.init(context);
    }


    @Test
    void convert() {
        assertEquals(now, converter.convert(date));
    }

    @Test
    void unconvert() {
        assertEquals(date, converter.unconvert(now));
    }
}
