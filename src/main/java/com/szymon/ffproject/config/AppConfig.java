package com.szymon.ffproject.config;

import com.google.gson.Gson;
import com.szymon.ffproject.dao.S3DAO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final S3DAO s3DAO;

    public AppConfig(S3DAO s3DAO) {this.s3DAO = s3DAO;}

    @Bean
    public DataCacheConfig getDataCacheConfig(Gson gson) {
        InputStream is = s3DAO.get(S3DAO.STATIC_DATA_FOLDER + "cacheConfig_2020-04-21T19:53:34.000").getInputStream();
        DataCacheConfig dataCacheConfig;
        try (Reader reader = new InputStreamReader(is)) {
            dataCacheConfig = gson.fromJson(reader, DataCacheConfig.class);
        } catch (IOException e) {
            dataCacheConfig = new DataCacheConfig();
        }
        return dataCacheConfig;
    }


}
