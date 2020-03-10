package com.szymon.ffproject.web.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.szymon.ffproject.database.entity.Event;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Bean;

public class CalendarConfig {

    private final List<String> plugins;
    private final String defaultView;
    private final Map<String, String> header;
    private final boolean eventLimit;
    private Set<Event> events;

    public CalendarConfig() {
        plugins = Arrays.asList("dayGrid", "timeGrid");
        defaultView = "timeGridWeek";
        header = new LinkedHashMap<>();
        header.put("left", "prev,next,today");
        header.put("center", "title");
        header.put("right", "dayGridMonth,timeGridWeek,timeGridDay");
        eventLimit = true;
    }

    @Bean
    public static Gson calendarConverter() {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).create();
    }

    public void setEvents(Set<Event> eventList) {
        this.events = eventList;
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDateTime> {


        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDateTime.toString());
        }
    }
}
