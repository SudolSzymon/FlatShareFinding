package com.szymon.ffproject.dao;

import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.cache.DataCache;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class HouseDAO extends CachedDAO<Household, String> {

    final HouseholdRepository repository;

    public HouseDAO(DataCache dataCache, HouseholdRepository repository) {
        super(dataCache);
        this.repository = repository;
    }

    @Override
    protected CrudRepository<Household, String> getRepository() {
        return repository;
    }

    @Override
    protected void refreshCache(Household household) {
        getCache().put(household.getName(), household);
    }

    @Override
    protected LoadingCache<String, Household> getCache() {
        return dataCache.getHouseCache();
    }

}
