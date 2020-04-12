package com.szymon.ffproject.service;

import com.szymon.ffproject.controller.GenericController;
import com.szymon.ffproject.dao.HouseDAO;
import com.szymon.ffproject.database.entity.Expense;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.ShopItem;
import com.szymon.ffproject.database.entity.ShopList;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.util.EntityUtil;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class HouseholdService {

    public static final String HOUSE_ADMIN_PREFIX = "HAdmin-";
    private final HouseDAO houseDAO;
    private final PasswordEncoder passwordEncoder;


    public HouseholdService(HouseDAO houseDAO, PasswordEncoder passwordEncoder) {
        this.houseDAO = houseDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Household getHousehold(String name) {
        Household house = houseDAO.get(name);
        if (house != null)
            return house;
        throw GenericController.badRequestException("House not found");
    }

    public boolean exist(String name) {
        return houseDAO.exist(name);
    }

    public void newHousehold(Household household, HashSet<String> members) {
        household.encrypt(passwordEncoder);
        household.setMembers(members);
        houseDAO.save(household);
    }

    public void updateHouse(Household house, Household oldHouse) {
        EntityUtil.update(house, oldHouse);
        houseDAO.save(house);
    }

    public void addMember(String name, Principal principal) {
        Household house = getHousehold(name);
        Set<String> members = house.getMembers();
        members.add(principal.getName());
        houseDAO.save(house);
    }

    public boolean verifyPassword(String name, String pass) {
        return passwordEncoder.matches(pass, getHousehold(name).getPassword());
    }

    public void addShopList(Household household, ShopList list) {
        household.addList(list);
        houseDAO.save(household);
    }

    public void removeShopList(Household household, Integer index) {
        String name = getListByIndex(household, index).getName();
        household.getLists().remove(name);
        houseDAO.save(household);
    }

    public ShopList getListByIndex(Household household, @PathVariable Integer index) {
        return household.getLists().values().toArray(new ShopList[0])[index];
    }

    public void addShopListItem(Household household, String listName, ShopItem item) {
        household.getList(listName).getItemList().add(item);
        houseDAO.save(household);
    }

    public void removeShopListItem(Household household, String listName, int index) {
        household.getList(listName).getItemList().remove(index);
        houseDAO.save(household);
    }

    public void delete(Household household) {
        houseDAO.delete(household.getName());
    }

    public boolean addExpense(Household household, Expense expense) {
        Set<String> members = household.getMembers();
        if (members.contains(expense.getBorrower()) && members.contains(expense.getLender())) {
            household.addExpense(expense);
            houseDAO.save(household);
            return true;
        } else
            return false;
    }

    public boolean deleteExpense(Household household, Integer index, User user) {
        Expense expense = household.getExpenses().get(index);
        if (expense.getLender().equals(user.getName())) {
            household.getExpenses().remove(expense);
            houseDAO.save(household);
            return true;
        } else
            return false;
    }
}