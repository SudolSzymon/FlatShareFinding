package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.cache.DataCache;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class HouseholdDAO extends CachedDAO<Household> {

    final HouseholdRepository repository;
    private final DataCache<String, Household> cache;

    public HouseholdDAO(DataCache<String, Household> cache, HouseholdRepository repository) {
        this.repository = repository;
        this.cache = cache;
    }

    @Override
    protected CrudRepository<Household, String> getRepository() {
        return repository;
    }

    @Override
    protected LoadingCache<String, Optional<Household>> getCache() {
        return cache.getCache();
    }

}
