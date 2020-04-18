package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import java.util.Optional;
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
        Optional<T> cached = getCache().getIfPresent(id);
        return cached != null && cached.isPresent() || getRepository().existsById(id);
    }

    @Override
    public final void delete(U id) {
        getRepository().deleteById(id);
        getCache().invalidate(id);
    }


    @Override
    public final T get(U id) {
        return get(id, false);
    }


    public final T get(U id, boolean noCache) {
        if (noCache)
            return getRepository().findById(id).orElse(null);
        try {
            return getCache().get(id).orElse(null);
        } catch (ExecutionException e) {
            logger.error("Failed to load object from " + this.getClass().getName(), e);
            return null;
        }
    }


    protected abstract CrudRepository<T, U> getRepository();

    protected abstract void refreshCache(T object);

    protected abstract LoadingCache<U, Optional<T>> getCache();


}
