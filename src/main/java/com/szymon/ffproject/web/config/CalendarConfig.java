package com.szymon.ffproject.web.config;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.szymon.ffproject.database.entity.Event;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;

public class CalendarConfig {

    private String defaultView;
    private Map<String, String> header;
    private LocalDate defaultDate;
    private boolean navLinks;
    private boolean editable;
    private boolean eventLimit;
    private boolean displayEventTime;
    private List<Event> events;
   // private Map<String, Map<String, Object>> views;

    public CalendarConfig() {
        defaultView = "agendaWeek";
        header = new LinkedHashMap<>();
        header.put("left", "prev,next,today");
        header.put("center", "title");
        header.put("right", "month,agendaWeek");
        this.navLinks = true;
        this.eventLimit = true;
    }

    private LinkedHashMap<String, Object> getTimeFormat() {
        LinkedHashMap<String, Object> eventTimeFormat = new LinkedHashMap<>();
        eventTimeFormat.put("hour", "2-digit");
        eventTimeFormat.put("minute", "2-digit");
        eventTimeFormat.put("hour12", false);
        return eventTimeFormat;
    }

    @Bean
    public static Gson calendarConverter() {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).create();
    }

    public void setEvents(List<Event> eventList) {
        this.events = eventList;
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDateTime> {


        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDateTime.toString());
        }
    }
}
