package com.szymon.ffproject.database.repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.database.entity.User;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataCache {

    public static final String ALL_USERS = "ALL_USERS";

    private final LoadingCache<String, Iterable<User>> userCache;


    public DataCache(UserRepository userRepository) {
        userCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMillis(10))
            .build(
                new CacheLoader<String, Iterable<User>>() {
                    @Override
                    public Iterable<User> load(String key) {
                        if (key.equals(ALL_USERS))
                            return userRepository.findAll();
                        return Collections.emptyList();
                    }
                });
    }

    public Iterable<User> getUsers(String key) {
        try {
            return userCache.get(key);
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }

    public List<User> findFilteredUsersList(DataCacheFilter<User> filter) {
        Iterable<User> users = getUsers(DataCache.ALL_USERS);
        return filter.filter(users);

    }



}
