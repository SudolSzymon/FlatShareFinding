package com.szymon.ffproject.cache;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface DataCacheFilter<T> {

    List<T> filter(Iterable<T> initial);

    default <U> Predicate<? super T> withInPred(U min, U max, Function<T, Comparable<U>> valueExtractor) {
        return u -> {
            Comparable<U> val = valueExtractor.apply(u);
            return !(val == null && (min != null || max != null)) && (min == null || val.compareTo(min) > 0) && (max == null || val.compareTo(max) < 0);
        };
    }
}
