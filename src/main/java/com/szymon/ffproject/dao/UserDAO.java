package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.cache.DataCache;
import com.szymon.ffproject.cache.UserDataCacheFilter;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDAO extends CachedDAO<User, String> {

    final UserRepository repository;

    public UserDAO(DataCache dataCache, UserRepository repository) {
        super(dataCache);
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
        return dataCache.getUserCache();
    }

    public List<User> get(UserDataCacheFilter filter) {
        try {
            List<User> list = dataCache.getUserListCache().getIfPresent(filter);
            if (list != null) {
                list = dataCache.getUserListCache().get(new UserDataCacheFilter());
                dataCache.getUserListCache().put(filter, list = filter.filter(list));
            }
            return list;
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }
}
