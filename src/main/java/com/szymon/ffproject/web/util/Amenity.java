package com.szymon.ffproject.web.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Amenity {
    GARAGE("garage"), BALCONY("balcony"), TV("tv");


    private final String name;

    Amenity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Set<String> stringValues(){
        return Arrays.stream(values()).map(Amenity::toString).collect(Collectors.toSet());
    }


}
