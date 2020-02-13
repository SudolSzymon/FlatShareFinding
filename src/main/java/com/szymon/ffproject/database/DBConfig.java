package com.szymon.ffproject.database;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableDynamoDBRepositories(basePackages = "com.szymon.ffproject.database.repository")
@ImportResource({"classpath*:DBContext.xml"})
public class DBConfig {

    @Autowired
    AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withCredentials(awsStaticCredentialsProvider)
            .withEndpointConfiguration(new EndpointConfiguration("http://localhost:8000", Regions.EU_WEST_2.getName()))
            .build();

    }

}
