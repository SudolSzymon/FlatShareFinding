package com.szymon.ffproject.database.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@DynamoDBTable(tableName = "AuthenticationUsers")
public class AuthenticationUser {


    private String name;
    private List<String> roles;
    private String password;

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public AuthenticationUser setName(String name) {
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

    public AuthenticationUser setRoles(String... roles) {
        this.roles = Arrays.asList(roles);
        return this;
    }

    public AuthenticationUser setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }


    @DynamoDBAttribute
    public String getPassword() {
        return password;
    }

    public AuthenticationUser setPassword(String password) {
        this.password = password;
        return this;
    }


}
