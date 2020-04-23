package com.szymon.ffproject.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DateUtilTest {

    String now = "2020-04-20 10:30";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
    void toDate() throws ParseException {
        LocalDateTime dateTime = LocalDateTime.parse(now, dateTimeFormatter);
        assertEquals(simpleDateFormat.parse(now), DateUtil.toDate(dateTime));

    }

    @Test
    void toLocalDateTime() throws ParseException {
        Date date = simpleDateFormat.parse(now);
        assertEquals(LocalDateTime.parse(now, dateTimeFormatter), DateUtil.toLocalDateTime(date));
    }
}
