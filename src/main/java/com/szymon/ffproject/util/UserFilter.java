package com.szymon.ffproject.util;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.util.annotation.InputType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserFilter implements Filter<User> {

    private String city;
    @InputType(type = "number")
    private Double minBudget;
    @InputType(type = "number")
    private Double maxBudget;
    @InputType(type = "valueSelect")
    private List<String> amenities;
    @InputType(type = "valueSelect")
    private List<String> houseTypes;
    @InputType(type = "valueSelect")
    private List<String> gender;
    @InputType(type = "number")
    private Integer flatSizeMin;
    @InputType(type = "number")
    private Integer flatSizeMax;
    @InputType(type = "number")
    private Integer ageMin;
    @InputType(type = "number")
    private Integer ageMax;
    @InputType(type = "number")
    private Integer distanceMin;
    @InputType(type = "number")
    private Integer distanceMax;
    @InputType(type = "boolean")
    private Boolean furnished;
    @InputType(type = "boolean")
    private Boolean unfurnished;
    @InputType(type = "number")
    private Integer countMin;
    @InputType(type = "number")
    private Integer countMax;
    @InputType(type = "number")
    private Integer rentLengthMin;
    @InputType(type = "number")
    private Integer rentLengthMax;


    public List<User> filter(Iterable<User> initial) {
        return StreamSupport.stream(initial.spliterator(), false)
            .filter(withInPred(minBudget, maxBudget, User::getMonthlyBudget))
            .filter(withInPred(ageMin, ageMax, User::getAge))
            .filter(withInPred(countMin, countMax, User::getCount))
            .filter(withInPred(rentLengthMin, rentLengthMax, User::getRent))
            .filter(user -> amenities == null || user.getAmenities().containsAll(amenities))
            .filter(user -> houseTypes == null || user.getHouseType().containsAll(houseTypes))
            .filter(withInPred(flatSizeMin, flatSizeMax, User::getHouseSize))
            .filter(withInPred(distanceMin, distanceMax, User::getDistanceToCenter))
            .filter(user -> gender == null || user.getGender().containsAll(gender))
            .filter(user -> furnished == null || user.getFurnished() != null && user.getFurnished().equals(furnished))
            .filter(user -> unfurnished == null || user.getUnfurnished() != null && user.getUnfurnished().equals(unfurnished))
            .filter(user -> city == null || city.isEmpty() || city.equalsIgnoreCase(user.getCity()))
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

    public Boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(Boolean furnished) {
        this.furnished = furnished;
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public Boolean getUnfurnished() {
        return unfurnished;
    }

    public void setUnfurnished(Boolean unfurnished) {
        this.unfurnished = unfurnished;
    }


    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }


    public Integer getCountMin() {
        return countMin;
    }

    public void setCountMin(Integer countMin) {
        this.countMin = countMin;
    }

    public Integer getCountMax() {
        return countMax;
    }

    public void setCountMax(Integer countMax) {
        this.countMax = countMax;
    }

    public Integer getRentLengthMin() {
        return rentLengthMin;
    }

    public void setRentLengthMin(Integer rentLengthMin) {
        this.rentLengthMin = rentLengthMin;
    }

    public Integer getRentLengthMax() {
        return rentLengthMax;
    }

    public void setRentLengthMax(Integer rentLengthMax) {
        this.rentLengthMax = rentLengthMax;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return AppContext.getBeanByClass(Gson.class).toJson(this);
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
        if (!Objects.equals(this.amenities, fObj.amenities))
            return false;
        if (!Objects.equals(this.minBudget, fObj.minBudget))
            return false;
        if (!Objects.equals(this.maxBudget, fObj.maxBudget))
            return false;
        if (!Objects.equals(this.distanceMin, fObj.distanceMin))
            return false;
        if (!Objects.equals(this.distanceMax, fObj.distanceMax))
            return false;
        if (!Objects.equals(this.flatSizeMax, fObj.flatSizeMax))
            return false;
        if (!Objects.equals(this.ageMin, fObj.ageMin))
            return false;
        if (!Objects.equals(this.ageMax, fObj.ageMax))
            return false;
        if (!Objects.equals(this.countMin, fObj.countMin))
            return false;
        if (!Objects.equals(this.countMax, fObj.countMax))
            return false;
        if (!Objects.equals(this.rentLengthMax, fObj.rentLengthMax))
            return false;
        if (!Objects.equals(this.rentLengthMin, fObj.rentLengthMin))
            return false;
        if (!Objects.equals(this.gender, fObj.gender))
            return false;
        if (!Objects.equals(this.city, fObj.city))
            return false;
        return Objects.equals(this.houseTypes, fObj.houseTypes);
    }

}

