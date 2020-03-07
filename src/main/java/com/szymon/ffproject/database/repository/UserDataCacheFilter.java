package com.szymon.ffproject.database.repository;

import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.web.util.annotation.InputType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserDataCacheFilter implements DataCacheFilter<User> {

    private Double minBudget;
    private Double maxBudget;
    @InputType(type = "valueSelect")
    private List<String> amenities;
    @InputType(type = "valueSelect")
    private List<String> houseTypes;
    private Integer flatSizeMin;
    private Integer flatSizeMax;
    private Integer distanceMin;
    private Integer distanceMax;


    public List<User> filter(Iterable<User> initial) {
        return StreamSupport.stream(initial.spliterator(), false)
            .filter(withInPred(minBudget, maxBudget, User::getMonthlyBudget))
            .filter(user -> amenities == null || user.getAmenities().containsAll(amenities))
            .filter(user -> houseTypes == null || user.getHouseTypes().containsAll(houseTypes))
            .filter(withInPred(flatSizeMin, flatSizeMax, User::getHouseSize))
            .filter(withInPred(distanceMin, distanceMax, User::getDistanceToCenter))
            .collect(Collectors.toList());
    }


    public Integer getDistanceMax() {
        return distanceMax;
    }

    public void setDistanceMax(Integer distanceMax) {
        this.distanceMax = distanceMax;
    }

    public Integer getDistanceMin() {
        return distanceMin;
    }

    public void setDistanceMin(Integer distanceMin) {
        this.distanceMin = distanceMin;
    }

    public Integer getFlatSizeMax() {
        return flatSizeMax;
    }

    public void setFlatSizeMax(Integer flatSizeMax) {
        this.flatSizeMax = flatSizeMax;
    }

    public List<String> getHouseTypes() {
        return houseTypes;
    }

    public void setHouseTypes(List<String> houseTypes) {
        this.houseTypes = houseTypes;
    }

    public Integer getFlatSizeMin() {
        return flatSizeMin;
    }

    public void setFlatSizeMin(Integer flatSizeMin) {
        this.flatSizeMin = flatSizeMin;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public Double getMinBudget() {
        return minBudget;
    }

    public void setMinBudget(Double minBudget) {
        this.minBudget = minBudget;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }
}
