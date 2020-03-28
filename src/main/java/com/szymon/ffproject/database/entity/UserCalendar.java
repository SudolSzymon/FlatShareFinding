package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.util.ArrayList;
import java.util.List;

@DynamoDBDocument
public class UserCalendar {

    private List<Event> events;


    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<Event> getEvents() {
        if (events == null)
            events = new ArrayList<>();
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        getEvents().add(event);
    }
}
