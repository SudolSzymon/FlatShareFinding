package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class CachedDAO<T, U> implements DAO<T, U> {

    private static final Logger logger = LoggerFactory.getLogger(CachedDAO.class);

    @Override
    public final void save(T object) {
        getRepository().save(object);
        refreshCache(object);
    }

    @Override
    public final boolean exist(U id) {
        return getCache().getIfPresent(id) != null || getRepository().existsById(id);
    }

    @Override
    public final void delete(U id) {
        getRepository().deleteById(id);
        getCache().invalidate(id);
    }


    @Override
    public final T get(U id) {
        try {
            return getCache().get(id);
        } catch (ExecutionException e) {
            logger.error("Failed to load object from " + this.getClass().getName(), e);
            return null;
        }
    }


    protected abstract CrudRepository<T, U> getRepository();

    protected abstract void refreshCache(T object);

    protected abstract LoadingCache<U, T> getCache();


}
