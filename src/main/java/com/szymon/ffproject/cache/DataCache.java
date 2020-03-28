package com.szymon.ffproject.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import java.time.Duration;
import java.util.List;

public class DataCache {

    private final LoadingCache<DataCacheFilter<User>, List<User>> userListCache;

    private final LoadingCache<String, User> userCache;

    private final LoadingCache<String, Household> houseCache;


    public DataCache(UserRepository userRepository, HouseholdRepository householdRepository) {
        userListCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(120))
            .build(
                new CacheLoader<DataCacheFilter<User>, List<User>>() {
                    @Override
                    public List<User> load(DataCacheFilter<User> filter) {
                        return filter.filter(userRepository.findAll());
                    }
                });

        userCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build(
                new CacheLoader<String, User>() {
                    @Override
                    public User load(String name) {
                        return userRepository.findById(name).orElse(null);
                    }
                });

        houseCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build(
                new CacheLoader<String, Household>() {
                    @Override
                    public Household load(String name) {
                        return householdRepository.findById(name).orElse(null);
                    }
                });
    }


    public LoadingCache<String, User> getUserCache() {
        return userCache;
    }

    public LoadingCache<String, Household> getHouseCache() {
        return houseCache;
    }

    public LoadingCache<DataCacheFilter<User>, List<User>> getUserListCache() {
        return userListCache;
    }
}
