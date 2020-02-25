package com.szymon.ffproject.database.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.InputType;
import com.szymon.ffproject.web.util.annotation.Unmodifiable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;

@DynamoDBTable(tableName = "Households")
public class Household {

    @Unmodifiable
    private String name;
    @InputType(type = "pass")
    private String password;
    @FormTransient
    private Set<String> members;
    @FormTransient
    private List<Expense> expenses;
    @FormTransient
    private Map<String, List<ShopItem>> lists;

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBTyped(DynamoDBAttributeType.SS)
    public Set<String> getMembers() {
        return members;
    }


    public void setMembers(Set<String> members) {
        this.members = members;
    }


    @DynamoDBAttribute
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(Expense expense) {
        if(expenses == null)
            expenses = new ArrayList<>();
        expenses.add(expense);
    }

    public void encrypt(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    @DynamoDBTyped(DynamoDBAttributeType.M)
    public Map<String, List<ShopItem>> getLists() {
        if(lists == null)
            lists = new HashMap<>();
        return lists;
    }

    public void setLists(Map<String, List<ShopItem>> lists) {
        this.lists = lists;
    }
}

