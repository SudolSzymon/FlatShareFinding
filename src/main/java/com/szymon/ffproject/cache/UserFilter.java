package com.szymon.ffproject.cache;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.util.annotation.InputType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserFilter implements Filter<User> {

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

    @Override
    public String toString() {
       return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof UserFilter))
            return false;
        UserFilter fObj = (UserFilter) obj;
        if (!Objects.equals(this.amenities,fObj.amenities))
            return false;
        if (!Objects.equals(this.minBudget,fObj.minBudget))
            return false;
        if (!Objects.equals(this.maxBudget,fObj.maxBudget))
            return false;
        if (!Objects.equals(this.distanceMin,fObj.distanceMin))
            return false;
        if (!Objects.equals(this.distanceMax,fObj.distanceMax))
            return false;
        if (!Objects.equals(this.flatSizeMax,fObj.flatSizeMax))
            return false;
        if (!Objects.equals(this.flatSizeMin,fObj.flatSizeMin))
            return false;
        return Objects.equals(this.houseTypes, fObj.houseTypes);
    }
}
