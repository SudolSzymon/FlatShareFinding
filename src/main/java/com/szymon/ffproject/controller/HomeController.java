package com.szymon.ffproject.controller;

import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends GenericController {


    public HomeController(HouseholdService householdService,
                          UserService userService) {
        super(householdService, userService);
    }

    @RequestMapping({"/home", "/"})
    public String home() {
        return "home";
    }


}
