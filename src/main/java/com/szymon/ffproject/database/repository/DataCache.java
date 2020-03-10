package com.szymon.ffproject.database.repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.database.entity.User;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.jws.soap.SOAPBinding.Use;

public class DataCache {

    private final LoadingCache<DataCacheFilter<User>, List<User>> userCache;


    public DataCache(UserRepository userRepository) {
        userCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build(
                new CacheLoader<DataCacheFilter<User>, List<User>>() {
                    @Override
                    public List<User> load(DataCacheFilter<User> filter) {
                        return filter.filter(userRepository.findAll());
                    }
                });
    }

    public List<User> getUsers(DataCacheFilter<User> filter) {
        try {
            return userCache.get(filter);
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }

}
