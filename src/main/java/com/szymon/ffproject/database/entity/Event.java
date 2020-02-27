package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.InputType;
import com.szymon.ffproject.database.converter.DateTimeConverter;
import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@DynamoDBDocument
public class Event {

    @NotBlank
    private String title;
    @Future
    @InputType(type = "dateTime")
    private LocalDateTime start;
    @Future
    @InputType(type = "dateTime")
    private LocalDateTime end;
    @FormTransient
    private final boolean allDay = false;
    @Size(min = 1, max = 1000)
    private transient Set<String> participants;


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

    @DynamoDBTyped(DynamoDBAttributeType.SS)
    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }
}
