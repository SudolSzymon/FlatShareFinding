package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.szymon.ffproject.util.annotation.DisplayAs;
import com.szymon.ffproject.util.annotation.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@DynamoDBDocument
public class ShopList {
    @NotBlank
    private String name;
    @Size(max = 100)
    @DisplayAs(display = "Item list")
    @Transient
    private List<ShopItem> itemList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShopItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ShopItem> itemList) {
        this.itemList = itemList;
    }


    @Override
    public boolean equals(Object obj) {
       return obj instanceof ShopList && Objects.equals(name, ((ShopList) obj).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
