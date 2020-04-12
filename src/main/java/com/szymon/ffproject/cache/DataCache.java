package com.szymon.ffproject.cache;

import com.google.common.cache.LoadingCache;


@FunctionalInterface
public interface DataCache<K, V> {

    LoadingCache<K, V> getCache();
}
