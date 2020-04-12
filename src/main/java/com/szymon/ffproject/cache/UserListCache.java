package com.szymon.ffproject.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.config.DataCacheConfig;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserListCache implements DataCache<UserFilter, List<User>> {

    private final LoadingCache<UserFilter, List<User>> userListCache;

    public UserListCache(UserRepository userRepository, DataCacheConfig config) {
        userListCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.parseInt(config.get("userListCache", "maxSize")))
            .expireAfterAccess(Duration.ofMinutes(Integer.parseInt(config.get("userListCache", "accessCacheTime"))))
            .expireAfterWrite(Duration.ofMinutes(Integer.parseInt(config.get("userListCache", "writeCacheTime"))))
            .build(
                new CacheLoader<Filter<User>, List<User>>() {
                    @Override
                    public List<User> load(Filter<User> filter) {
                        return filter.filter(userRepository.findAll());
                    }
                });
    }

    @Override
    public LoadingCache<UserFilter, List<User>> getCache() {
        return userListCache;
    }
}