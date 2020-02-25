package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends GenericController {


    public HomeController(UserRepository repository, HouseholdRepository repositoryH) {
        super(repository, repositoryH);
    }

    @RequestMapping({"/home", "/"})
    public String home() {
        return "home";
    }


}
