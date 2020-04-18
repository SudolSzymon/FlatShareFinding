package com.szymon.ffproject.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Gender {
    MALE("male"), FEMALE("female"), OTHER("other");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public static Set<String> stringValues() {
        return Arrays.stream(values()).map(Gender::toString).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return name;
    }


}
