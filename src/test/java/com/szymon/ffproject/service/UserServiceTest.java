package com.szymon.ffproject.service;

import static com.szymon.ffproject.service.HouseholdService.HOUSE_ADMIN_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository repository;

    @Autowired
    UserService service;

    @Mock
    Principal principal;

    User user = new User();

    @BeforeEach
    void setUp() {
        user.setName("test");
        when(principal.getName()).thenReturn("test");
    }

    @Test
    void assignHouse() {
        service.assignHouse("house", user, true);
        assertTrue(user.getRoles().contains(HOUSE_ADMIN_PREFIX + "house"));
        assertEquals("house", user.getHouseName());
    }

    @Test
    void newUser() {
        user.setPassword("test");
        service.newUser(user);
        assertTrue(user.getRoles().contains("user") && user.getRoles().size() == 1);
    }
}
