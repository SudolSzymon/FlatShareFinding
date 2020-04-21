package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@DynamoDBDocument
public class ShopItem extends DBEntity {

    @NotBlank
    private String name;

    @Positive
    private double count;


    @DynamoDBAttribute
    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    @DynamoDBAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String toString(){
        return getName() + " X " + getCount();
    }
}
