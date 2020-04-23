package com.szymon.ffproject.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.szymon.ffproject.database.entity.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserFilterTest {

    static List<User> list = new ArrayList<>();
    static User userA = new User();
    static User userB = new User();
    static User userC = new User();
    static User userD = new User();

    @BeforeAll
    static void setUp() {
        userA.setAge(5);
        userB.setAge(10);
        userC.setAge(40);
        userC.setCity("london");
        userB.setCity("London");
        userD.setCity("Berlin");
        userA.setAmenities(Arrays.asList("TV", "Garage"));
        userB.setAmenities(Collections.singletonList("TV"));
        userC.setAmenities(Arrays.asList("TV", "Garage", "bath"));
        list = Arrays.asList(userA, userB, userC, userD);
    }

    @Test
    void filterWithinPred() {
        UserFilter filter = new UserFilter();
        filter.setAgeMax(15);
        filter.setAgeMin(7);
        List<User> result = filter.filter(list);
        assertTrue(result.contains(userB));
        assertEquals(1, result.size());
    }

    @Test
    void filterEquals() {
        UserFilter filter = new UserFilter();
        filter.setCity("londoN");
        List<User> result = filter.filter(list);
        assertTrue(result.contains(userB));
        assertTrue(result.contains(userC));
        assertEquals(2, result.size());
    }

    @Test
    void filterContains() {
        UserFilter filter = new UserFilter();
        filter.setAmenities(Arrays.asList("TV", "Garage"));
        List<User> result = filter.filter(list);
        assertTrue(result.contains(userA));
        assertTrue(result.contains(userC));
        assertEquals(2, result.size());
    }


}
