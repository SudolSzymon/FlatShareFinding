package com.szymon.ffproject.database.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.Unmodifiable;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;


@DynamoDBTable(tableName = "Users")
public class User {

    @Unmodifiable
    private String name;
    @FormTransient
    private List<String> roles;
    @FormTransient
    private String password;
    @FormTransient
    private UserCalendar calendar;
    @Unmodifiable
    private String houseName;
    private String description;

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<String> getRoles() {
        return roles;
    }

    @DynamoDBIgnore
    public String[] getRolesAsArray() {
        return roles.toArray(new String[0]);
    }

    public User setRoles(String... roles) {
        this.roles = Arrays.asList(roles);
        return this;
    }

    public User setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }


    @DynamoDBAttribute
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User encrypt(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
        return this;
    }

    @DynamoDBAttribute
    public UserCalendar getCalendar() {
        return calendar;
    }

    public User setCalendar(UserCalendar calendar) {
        this.calendar = calendar;
        return this;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute
    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }
}
