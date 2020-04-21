package com.szymon.ffproject.service;

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
        return houseDAO.get(name);
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
        Household household = getHousehold(name);
        return household != null && passwordEncoder.matches(pass, household.getPassword());
    }

    public void addShopList(Household household, ShopList list) {
        addList(household, list);
        houseDAO.save(household);
    }

    public void removeShopList(Household household, String id) {
        household.getLists().remove(id);
        houseDAO.save(household);
    }

    public void addShopListItem(Household household, String id, ShopItem item) {
        getList(household, id).getItems().put(item.getEntityID(), item);
        houseDAO.save(household);
    }

    public void removeShopListItem(Household household, String listID, String itemID) {
        getList(household, listID).getItems().remove(itemID);
        houseDAO.save(household);
    }

    public ShopList getList(Household household, String id) {
        return household.getLists().get(id);
    }

    public void addList(Household household, ShopList list) {
        household.getLists().put(list.getEntityID(), list);
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

    public boolean deleteExpense(Household household, String id, User user) {
        Expense expense = household.getExpenses().get(id);
        if (expense.getLender().equals(user.getName())) {
            household.getExpenses().remove(expense.getEntityID());
            houseDAO.save(household);
            return true;
        } else
            return false;
    }


}
