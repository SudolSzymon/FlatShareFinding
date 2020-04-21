package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.szymon.ffproject.database.converter.DateTimeConverter;
import java.time.LocalDateTime;
import java.util.UUID;

@DynamoDBTable(tableName = "")
public abstract class DBEntity implements Entity {

    private LocalDateTime creationTime;

    private LocalDateTime modificationTime;

    private String entityID;

    public DBEntity() {
        entityID = UUID.randomUUID().toString();
        creationTime = LocalDateTime.now();
    }

    @DynamoDBAttribute
    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String id) {
        entityID = id;
    }

    @DynamoDBTypeConverted(converter = DateTimeConverter.class)
    public final LocalDateTime getCreationTime() {
        return creationTime;
    }

    public final void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }


    @DynamoDBTypeConverted(converter = DateTimeConverter.class)
    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(LocalDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }
}
