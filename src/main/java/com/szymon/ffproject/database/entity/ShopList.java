package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.szymon.ffproject.util.annotation.DisplayAs;
import com.szymon.ffproject.util.annotation.Transient;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@DynamoDBDocument
public class ShopList extends DBEntity {

    @NotBlank
    private String name;
    @DisplayAs(display = "Item list")
    @Transient
    private @Size(max = 100) Map<String, ShopItem> items = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Size(max = 100) Map<String, ShopItem> getItems() {
        return items;
    }

    public void setItems(@Size(max = 100) Map<String, ShopItem> items) {
        this.items = items;
    }
}
