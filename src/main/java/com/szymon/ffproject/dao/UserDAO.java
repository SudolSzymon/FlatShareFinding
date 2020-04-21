package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.cache.DataCache;
import com.szymon.ffproject.cache.UserFilter;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDAO extends CachedDAO<User> {

    private final DataCache<UserFilter, List<User>> userListDataCache;

    private final DataCache<String, User> userDataCache;

    private final UserRepository repository;

    public UserDAO(
        DataCache<UserFilter, List<User>> userListDataCache,
        DataCache<String, User> userDataCache,
        UserRepository repository) {
        this.userListDataCache = userListDataCache;
        this.userDataCache = userDataCache;
        this.repository = repository;
    }

    @Override
    protected CrudRepository<User, String> getRepository() {
        return repository;
    }


    protected LoadingCache<String, Optional<User>> getCache() {
        return userDataCache.getCache();
    }

    public List<User> get(UserFilter filter) {
        try {
            @Nullable Optional<List<User>> list = userListDataCache.getCache().getIfPresent(filter);
            if (list == null || !list.isPresent()) {
                list = userListDataCache.getCache().get(new UserFilter());
                userListDataCache.getCache().put(filter, list = Optional
                    .ofNullable(filter.filter(list.orElse(Collections.emptyList()))));
            }
            return list.orElse(Collections.emptyList());
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }

}
