package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.database.entity.DBEntity;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class CachedDAO<T extends DBEntity> extends DAO<T> {

    private static final Logger logger = LoggerFactory.getLogger(CachedDAO.class);

    @Override
    protected final void executeSave(T object) {
        getRepository().save(object);
        refreshCache(object);
    }

    @Override
    public final boolean exist(String id) {
        Optional<T> cached = getCache().getIfPresent(id);
        return cached != null && cached.isPresent() || getRepository().existsById(id);
    }

    @Override
    public final void delete(String id) {
        getRepository().deleteById(id);
        getCache().invalidate(id);
    }


    @Override
    public final T get(String id) {
        return get(id, false);
    }


    public final T get(String id, boolean noCache) {
        if (noCache)
            return getRepository().findById(id).orElse(null);
        try {
            return getCache().get(id).orElse(null);
        } catch (ExecutionException e) {
            logger.error("Failed to load object from " + this.getClass().getName(), e);
            return null;
        }
    }


    private void refreshCache(T object) {
        getCache().put(object.getEntityID(), Optional.of(object));
    }

    protected abstract CrudRepository<T, String> getRepository();

    protected abstract LoadingCache<String, Optional<T>> getCache();


}
