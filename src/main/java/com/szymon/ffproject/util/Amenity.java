package com.szymon.ffproject.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Amenity {
    GARAGE("garage"), BALCONY("balcony"), TV("tv"), BATHTUB("bathtub"), CONCIERGE("concierge");


    private final String name;

    Amenity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Set<String> stringValues() {
        return Arrays.stream(values()).map(Amenity::toString).collect(Collectors.toSet());
    }


}
