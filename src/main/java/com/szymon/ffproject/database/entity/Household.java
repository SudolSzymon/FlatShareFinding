package com.szymon.ffproject.database.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.util.annotation.InputType;
import com.szymon.ffproject.util.annotation.Transient;
import com.szymon.ffproject.util.annotation.Unmodifiable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

@DynamoDBTable(tableName = "Households")
public class Household extends DBEntity {

    @NotBlank
    @Unmodifiable
    private String name;
    @NotBlank
    @InputType(type = "pass")
    private String password;
    @Transient
    private Set<String> members;
    @Transient
    private Map<String, Expense> expenses;
    @Transient
    private Map<String, ShopList> lists;

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBTyped(DynamoDBAttributeType.SS)
    public Set<String> getMembers() {
        if (members == null)
            members = new HashSet<>();
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

    @DynamoDBTyped(DynamoDBAttributeType.M)
    public Map<String, Expense> getExpenses() {
        if (expenses == null)
            expenses = new LinkedHashMap<>();
        return expenses;
    }

    public void setExpenses(Map<String, Expense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(Expense expense) {
        if (expenses == null)
            expenses = new LinkedHashMap<>();
        expenses.put(expense.getEntityID(), expense);
    }

    public void encrypt(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    @DynamoDBTyped(DynamoDBAttributeType.M)
    public Map<String, ShopList> getLists() {
        if (lists == null)
            lists = new LinkedHashMap<>();
        return lists;
    }

    public void setLists(Map<String, ShopList> lists) {
        this.lists = lists;
    }

    @DynamoDBIgnore
    @Override
    public String getEntityID() {
        return name;
    }

}

