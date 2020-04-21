package com.szymon.ffproject.database.converter;

import static com.szymon.ffproject.util.AppContext.getBeanByClass;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    @Override
    public String convert(LocalDateTime localDateTime) {
        return localDateTime.format(getBeanByClass(DateTimeFormatter.class));
    }

    @Override
    public LocalDateTime unconvert(String s) {
        return LocalDateTime.parse(s, getBeanByClass(DateTimeFormatter.class));
    }
}
