package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.database.converter.DateTimeConverter;
import com.szymon.ffproject.util.annotation.DisplayAs;
import com.szymon.ffproject.util.annotation.InputType;
import com.szymon.ffproject.util.annotation.Private;
import com.szymon.ffproject.util.annotation.Transient;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@DynamoDBDocument
public class Event extends Entity {

    @NotBlank
    private String title;
    @Future
    @InputType(type = "dateTime")
    private LocalDateTime start;
    @Future
    @InputType(type = "dateTime")
    private LocalDateTime end;
    @DisplayAs(display = "Event color")
    @InputType(type = "color")
    private String backgroundColor = "#add8e6";
    @Transient
    @Private
    private final boolean allDay = false;
    @Size(min = 1, max = 1000)
    @InputType(type = "valueSelect")
    private transient Set<String> participants;
    @InputType(type = "boolean")
    private transient boolean repeat;
    @Transient
    @Private
    private String startTime;
    @Transient
    @Private
    private String endTime;
    @InputType(type = "valueSelect")
    @DisplayAs(display = "Repeats on")
    private transient Set<String> repeatOn;
    @Transient
    @Private
    private List<String> daysOfWeek;


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

    @DynamoDBAttribute
    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    @Override
    public int hashCode() {
        return Objects.hash(title, start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Event))
            return false;
        Event event = (Event) obj;
        return obj == this || (Objects.equals(title, event.title) && Objects.equals(start, event.start) && Objects
            .equals(end, event.end) && Objects.equals(endTime, event.endTime) && Objects.equals(startTime, event.startTime));
    }

    @DynamoDBAttribute
    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    @DynamoDBAttribute
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @DynamoDBAttribute
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void convertToRecurringEvent() {
        startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        start = null;
        endTime = end.format(DateTimeFormatter.ofPattern("HH:mm"));
        end = null;
        if(repeatOn!= null && !repeatOn.isEmpty())
            daysOfWeek =  repeatOn.stream().map(DayOfWeek::valueOf).map(DayOfWeek::getValue).map(String::valueOf).collect(Collectors.toList());
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public Set<String> getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(Set<String> repeatOn) {
        this.repeatOn = repeatOn;
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
