package com.szymon.ffproject.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.config.DataCacheConfig;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.time.Duration;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserCache implements DataCache<String, User> {

    private final LoadingCache<String, Optional<User>> userCache;

    public UserCache(UserRepository userRepository, DataCacheConfig config) {
        userCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.parseInt(config.get("userCache", "maxSize")))
            .expireAfterWrite(Duration.ofMinutes(Integer.parseInt(config.get("userCache", "writeCacheTime"))))
            .expireAfterAccess(Duration.ofMinutes(Integer.parseInt(config.get("userCache", "accessCacheTime"))))
            .build(
                new CacheLoader<String, Optional<User>>() {
                    @Override
                    public Optional<User> load(String name) {
                        return userRepository.findById(name);
                    }
                });
    }

    @Override
    public LoadingCache<String, Optional<User>> getCache() {
        return userCache;
    }
}
