package com.szymon.ffproject.cache;

import com.google.common.cache.LoadingCache;
import java.util.Optional;


@FunctionalInterface
public interface DataCache<K, V> {

    LoadingCache<K, Optional<V>> getCache();
}
