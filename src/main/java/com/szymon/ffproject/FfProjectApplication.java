package com.szymon.ffproject;

import com.szymon.ffproject.database.DBInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ImportAutoConfiguration
public class FfProjectApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(FfProjectApplication.class, args);

        DBInitializer initializer = (DBInitializer) applicationContext.getBean("DBInitializer");
        initializer.init();
    }

}