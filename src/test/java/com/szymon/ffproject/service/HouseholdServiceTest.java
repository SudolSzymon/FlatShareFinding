package com.szymon.ffproject.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.szymon.ffproject.database.entity.Expense;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import java.security.Principal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class HouseholdServiceTest {

    @MockBean
    HouseholdRepository repository;

    @Autowired
    HouseholdService service;

    @Mock
    Principal principal;

    Household house = new Household();

    @BeforeEach
    void setUp() {
        house = new Household();
        house.setName(UUID.randomUUID().toString());
        house.setPassword("pass");
        when(repository.findById(house.getName())).thenReturn(java.util.Optional.ofNullable(house));
        when(principal.getName()).thenReturn("test");
    }

    @Test
    void newHousehold() {
        service.newHousehold(house, principal);
        assertTrue(house.getMembers().contains(principal.getName()));
    }

    @Test
    void verifyPassword() {
        service.newHousehold(house, principal);
        assertTrue(service.verifyPassword(house.getName(), "pass"));
    }

    @Test
    void addExpense() {
        house.setMembers(Sets.newHashSet("test", "test2"));
        Expense exp = new Expense();
        exp.setAmount(10.0);
        exp.setLender("test");
        exp.setBorrower("test2");
        exp.setTitle("test");
        assertTrue(service.addExpense(house, exp));
        Expense exp2 = new Expense();
        exp2.setAmount(10.0);
        exp2.setLender("test3");
        exp2.setBorrower("test2");
        exp2.setTitle("testWrong");
        assertFalse(service.addExpense(house, exp2));
    }

    @Test
    void deleteExpense() {
        house.setMembers(Sets.newHashSet("test", "test2"));
        Expense exp = new Expense();
        exp.setAmount(10.0);
        exp.setLender("test");
        exp.setBorrower("test2");
        exp.setTitle("test");
        assertTrue(service.addExpense(house, exp));
        User user = new User();
        user.setName("test2");
        assertFalse(service.deleteExpense(house, exp.getEntityID(), user));
        user.setName("test");
        assertTrue(service.deleteExpense(house, exp.getEntityID(), user));
    }
}
