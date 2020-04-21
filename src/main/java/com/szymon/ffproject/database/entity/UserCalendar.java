package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.util.LinkedHashMap;
import java.util.Map;

@DynamoDBDocument
public class UserCalendar extends DBEntity {

    private Map<String, Event> events;


    @DynamoDBTyped(DynamoDBAttributeType.M)
    public Map<String, Event> getEvents() {
        if (events == null)
            events = new LinkedHashMap<>();
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        getEvents().put(event.getEntityID(), event);
    }
}
