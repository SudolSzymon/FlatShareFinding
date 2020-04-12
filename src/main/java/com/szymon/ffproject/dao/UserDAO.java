package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.cache.DataCache;
import com.szymon.ffproject.cache.UserFilter;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDAO extends CachedDAO<User, String> {

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

    @Override
    protected void refreshCache(User user) {
        getCache().put(user.getName(), user);
    }

    protected LoadingCache<String, User> getCache() {
        return userDataCache.getCache();
    }

    public List<User> get(UserFilter filter) {
        try {
            List<User> list = userListDataCache.getCache().getIfPresent(filter);
            if (list == null) {
                list = userListDataCache.getCache().get(new UserFilter());
                userListDataCache.getCache().put(filter, list = filter.filter(list));
            }
            return list;
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }
}