package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.InputType;
import com.szymon.ffproject.database.converter.DateTimeConverter;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@DynamoDBDocument
public class Event {

    private String title;
    @InputType(type = "dateTime")
    private LocalDateTime start;
    @InputType(type = "dateTime")
    private LocalDateTime end;
    @FormTransient
    private boolean allDay = false;
    private transient List<String> participants;



    @DynamoDBAttribute
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DynamoDBTypeConverted(converter = DateTimeConverter.class)
    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DynamoDBTypeConverted(converter = DateTimeConverter.class)
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
