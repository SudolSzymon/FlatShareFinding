package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.util.HashSet;
import java.util.Set;

@DynamoDBDocument
public class UserCalendar {

    private Set<Event> events;


    @DynamoDBTyped(DynamoDBAttributeType.L)
    public Set<Event> getEvents() {
        if (events == null)
            events = new HashSet<>();
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        getEvents().add(event);
    }
}
