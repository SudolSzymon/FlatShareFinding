package com.szymon.ffproject.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.config.DataCacheConfig;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class UserCache implements DataCache<String, User> {

    private final LoadingCache<String, User> userCache;

    public UserCache(UserRepository userRepository, DataCacheConfig config) {
        userCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.parseInt(config.get("userCache", "maxSize")))
            .expireAfterWrite(Duration.ofMinutes(Integer.parseInt(config.get("userCache", "writeCacheTime"))))
            .expireAfterAccess(Duration.ofMinutes(Integer.parseInt(config.get("userCache", "accessCacheTime"))))
            .build(
                new CacheLoader<String, User>() {
                    @Override
                    public User load(String name) {
                        return userRepository.findById(name).orElse(null);
                    }
                });
    }

    @Override
    public LoadingCache<String, User> getCache() {
        return userCache;
    }
}
