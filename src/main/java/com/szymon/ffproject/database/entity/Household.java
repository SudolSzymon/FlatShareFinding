package com.szymon.ffproject.database.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.web.util.annotation.FormTransient;
import com.szymon.ffproject.web.util.annotation.InputType;
import com.szymon.ffproject.web.util.annotation.Unmodifiable;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@DynamoDBTable(tableName = "Households")
public class Household {

    @Unmodifiable
    private String name;
    @InputType(type = "pass")
    private String password;
    @FormTransient
    private List<String> members;

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<String> getMembers() {
        return members;
    }


    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void encrypt(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }



}
