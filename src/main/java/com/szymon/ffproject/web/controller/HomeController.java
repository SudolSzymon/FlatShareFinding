package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {


    final
    HouseholdRepository repositoryH;


    final
    UserRepository repositoryU;




    private static final Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    public HomeController(Environment environment, HouseholdRepository repositoryH,
                          UserRepository repositoryU) {
        this.repositoryH = repositoryH;
        this.repositoryU = repositoryU;
    }


    @RequestMapping({"/home", "/"})
    public String home(Model model) {
        return "home";
    }


}
