package com.szymon.ffproject.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.config.DataCacheConfig;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class HouseCache implements DataCache<String, Household> {

    private final LoadingCache<String, Household> houseCache;

    public HouseCache(HouseholdRepository householdRepository, DataCacheConfig config) {
        houseCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.parseInt(config.get("houseCache", "maxSize")))
            .expireAfterWrite(Duration.ofMinutes(Integer.parseInt(config.get("houseCache", "writeCacheTime"))))
            .expireAfterAccess(Duration.ofMinutes(Integer.parseInt(config.get("houseCache", "accessCacheTime"))))
            .build(
                new CacheLoader<String, Household>() {
                    @Override
                    public Household load(String name) {
                        return householdRepository.findById(name).orElse(null);
                    }
                });
    }

    @Override
    public LoadingCache<String, Household> getCache() {
        return houseCache;
    }
}