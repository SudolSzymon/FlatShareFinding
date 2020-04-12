package com.szymon.ffproject.config;

import java.util.HashMap;
import java.util.Map;

public class DataCacheConfig {

    private Map<String, Map<String, String>> cacheConfig = new HashMap<>();
    private Map<String, String> globalCacheConfig = new HashMap<>();


    public Map<String, Map<String, String>> getCacheConfig() {
        return cacheConfig;
    }

    public void setCacheConfig(Map<String, Map<String, String>> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public Map<String, String> getGlobalCacheConfig() {
        return globalCacheConfig;
    }

    public void setGlobalCacheConfig(Map<String, String> globalCacheConfig) {
        this.globalCacheConfig = globalCacheConfig;
    }

    public String get(String cacheName, String property) {
        if (cacheConfig.containsKey(cacheName)) {
            Map<String, String> config = cacheConfig.get(cacheName);
            if (config.containsKey(property))
                return config.get(property);
        }
        return globalCacheConfig.get(property);
    }
}
