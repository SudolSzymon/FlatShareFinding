package com.szymon.ffproject.web.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum HouseType {
    FLAT("flat"), HOUSE("house"), STUDIO("studio");


    private final String name;

    HouseType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


    public static Set<String> stringValues(){
        return Arrays.stream(values()).map(HouseType::toString).collect(Collectors.toSet());
    }


}
