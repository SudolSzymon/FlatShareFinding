package com.szymon.ffproject;

import com.szymon.ffproject.database.DBInitializer;
import com.szymon.ffproject.util.AppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:AppContext.xml"})
@ImportAutoConfiguration
public class FfProjectApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(FfProjectApplication.class, args);
        AppContext.init(applicationContext);
        DBInitializer initializer = (DBInitializer) applicationContext.getBean("DBInitializer");
        initializer.init();
    }
}
