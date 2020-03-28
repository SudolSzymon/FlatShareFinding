package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.cache.DataCache;
import java.util.concurrent.ExecutionException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class CachedDAO<T, U> implements GenericDAO<T, U> {

    protected final DataCache dataCache;

    public CachedDAO(DataCache dataCache) {this.dataCache = dataCache;}

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
        getCache().invalidate(id);
        getRepository().deleteById(id);
    }


    @Override
    public final T get(U id) {
        try {
            return getCache().get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }


    protected abstract CrudRepository<T, U> getRepository();

    protected abstract void refreshCache(T object);

    protected abstract LoadingCache<U, T> getCache();


}
