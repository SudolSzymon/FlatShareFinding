package com.szymon.ffproject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.szymon.ffproject.cache.HouseholdCache;
import com.szymon.ffproject.cache.UserCache;
import com.szymon.ffproject.cache.UserListCache;
import com.szymon.ffproject.controller.CalendarController;
import com.szymon.ffproject.controller.ExpenseController;
import com.szymon.ffproject.controller.HomeController;
import com.szymon.ffproject.controller.HouseholdController;
import com.szymon.ffproject.controller.SearchController;
import com.szymon.ffproject.controller.ShoppingListController;
import com.szymon.ffproject.controller.UserController;
import com.szymon.ffproject.dao.HouseholdDAO;
import com.szymon.ffproject.dao.S3DAO;
import com.szymon.ffproject.dao.UserDAO;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FfProjectApplicationTest {

    @Autowired
    UserListCache userListCache;
    @Autowired
    UserCache userCache;
    @Autowired
    HouseholdCache householdCache;
    @Autowired
    private HomeController homeController;
    @Autowired
    private CalendarController calendarController;
    @Autowired
    private ExpenseController expenseController;
    @Autowired
    private HouseholdController householdController;
    @Autowired
    private SearchController searchController;
    @Autowired
    private UserController userController;
    @Autowired
    private ShoppingListController shoppingListController;
    @Autowired
    private HouseholdDAO householdDAO;
    @Autowired
    private S3DAO s3DAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HouseholdService householdService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private HouseholdRepository householdRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() throws IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields())
            assertNotNull(field.get(this));
    }

}
