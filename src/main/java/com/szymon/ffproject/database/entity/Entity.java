package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.UUID;

@DynamoDBTable(tableName = "")
public abstract class Entity {

    private String entityID;

    public Entity() {
        entityID = UUID.randomUUID().toString();
    }

    @DynamoDBAttribute
    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String id) {
        entityID = id;
    }


    public final boolean match(String id) {
        return this.entityID.equals(id);
    }
}
