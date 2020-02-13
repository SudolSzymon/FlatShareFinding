package com.szymon.ffproject.database;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.szymon.ffproject.database.entity.AuthenticationUser;
import com.szymon.ffproject.database.entity.HouseHold;
import com.szymon.ffproject.database.entity.User;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.guava.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//java -D"java.library.path=./DynamoDBLocal_lib" -jar DynamoDBLocal.jar
@Component
public class DBInitializer {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;


    private static final Logger logger = Logger.getLogger(DBInitializer.class);

    public  void  init(){
    try {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        createTable(User.class);
        createTable(HouseHold.class);
        createTable(AuthenticationUser.class);
    } catch (Exception e) {
        logger.error("Unable to initialise tables", e);
    }
    }

    private void createTable(Class<?> table) {
        CreateTableRequest tableRequest = dynamoDBMapper
            .generateCreateTableRequest(table);

        tableRequest.setProvisionedThroughput(
            new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
    }


}
