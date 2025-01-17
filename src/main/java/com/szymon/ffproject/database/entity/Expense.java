package com.szymon.ffproject.database.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.szymon.ffproject.util.annotation.InputType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@DynamoDBDocument
public class Expense extends DBEntity {

    @NotBlank
    private String title;

    @NotBlank
    @InputType(type = "valueSelect")
    private String borrower;

    @NotBlank
    @InputType(type = "valueSelect")
    private String lender;

    @Positive
    private  double amount;



    @DynamoDBAttribute
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute
    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    @DynamoDBAttribute
    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    @DynamoDBAttribute
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
