package com.szymon.ffproject.database.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.szymon.ffproject.util.annotation.DisplayAs;
import com.szymon.ffproject.util.annotation.InputType;
import com.szymon.ffproject.util.annotation.Private;
import com.szymon.ffproject.util.annotation.Transient;
import com.szymon.ffproject.util.annotation.Unmodifiable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;


@DynamoDBTable(tableName = "Users")
public class User {

    @NotBlank
    @Unmodifiable
    private String name;
    @Transient
    @Private
    private List<String> roles;
    @Transient
    @Private
    private String password;
    @Transient
    @Private
    private UserCalendar calendar;
    @Unmodifiable
    @Private
    private String houseName;
    private String description;
    @Email
    private String email;
    @Pattern(regexp = "(^$)|(^\\+(?:[0-9]●?){6,14}[0-9]$)", message = "Please enter valid phone number prefixed with country code")
    private String phone;
    @Positive
    @Max(100000)
    @DisplayAs(display = "Monthly Budget")
    @InputType(type = "number")
    @Private
    private Double monthlyBudget;
    @Positive
    @Max(1000)
    @InputType(type = "number")
    @DisplayAs(display = "Distance to center in minutes")
    @Private
    private Integer distanceToCenter;
    @Size(max = 10)
    @InputType(type = "valueSelect")
    @DisplayAs(display = "Expected amenities")
    @Private
    private List<String> amenities;
    @Size(max = 10)
    @InputType(type = "valueSelect")
    @Private
    private List<String> houseTypes;
    @Positive
    @DisplayAs(display = "Flat size in meters")
    @InputType(type = "number")
    @Private
    private Integer houseSize;
    @Transient
    @Private
    private String avatarUniqueName;

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
        if (roles == null)
            roles = new ArrayList<>();
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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
        if (calendar == null)
            calendar = new UserCalendar();
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

    @DynamoDBAttribute
    public Double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(Double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    @DynamoDBAttribute
    public Integer getDistanceToCenter() {
        return distanceToCenter;
    }

    public void setDistanceToCenter(Integer distanceToCenter) {
        this.distanceToCenter = distanceToCenter;
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<String> getAmenities() {
        if (amenities == null)
            amenities = new ArrayList<>();
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    @DynamoDBAttribute
    public Integer getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(Integer houseSize) {
        this.houseSize = houseSize;
    }

    @DynamoDBTyped(DynamoDBAttributeType.L)
    public List<String> getHouseTypes() {
        if (this.houseTypes == null)
            houseTypes = new ArrayList<>();
        return houseTypes;
    }

    public void setHouseTypes(List<String> houseTypes) {
        this.houseTypes = houseTypes;
    }

    @DynamoDBAttribute
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @DynamoDBAttribute
    public String getAvatarUniqueName() {
        return avatarUniqueName;
    }

    public void setAvatarUniqueName(String avatarUniqueName) {
        this.avatarUniqueName = avatarUniqueName;
    }

}
