package com.szymon.ffproject.config;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class AppConfig {

    @Bean
    public DataCacheConfig getDataCacheConfig(Gson gson) throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:cacheConfig.json");
        DataCacheConfig dataCacheConfig;
        try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
            dataCacheConfig = gson.fromJson(reader, DataCacheConfig.class);
        } catch (IOException e) {
            dataCacheConfig = new DataCacheConfig();
        }
        return dataCacheConfig;
    }
}
