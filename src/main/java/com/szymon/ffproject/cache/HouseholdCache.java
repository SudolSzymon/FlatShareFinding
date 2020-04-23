package com.szymon.ffproject.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.szymon.ffproject.config.DataCacheConfig;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import java.time.Duration;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class HouseholdCache implements DataCache<String, Household> {

    private final LoadingCache<String, Optional<Household>> houseCache;

    public HouseholdCache(HouseholdRepository householdRepository, DataCacheConfig config) {
        houseCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.parseInt(config.get("houseCache", "maxSize")))
            .expireAfterWrite(Duration.ofMinutes(Integer.parseInt(config.get("houseCache", "writeCacheTime"))))
            .expireAfterAccess(Duration.ofMinutes(Integer.parseInt(config.get("houseCache", "accessCacheTime"))))
            .build(
                new CacheLoader<String, Optional<Household>>() {
                    @Override
                    public Optional<Household> load(String name) {
                        return householdRepository.findById(name);
                    }
                });
    }

    @Override
    public LoadingCache<String, Optional<Household>> getCache() {
        return houseCache;
    }
}
