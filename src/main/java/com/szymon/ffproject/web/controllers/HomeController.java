package com.szymon.ffproject.web.controllers;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.HouseHold;
import com.szymon.ffproject.database.repository.HouseHoldRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import java.util.Date;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    private final String appMode;
    @Autowired
    UserRepository repository;

    @Autowired
    HouseHoldRepository  repositoryH;


    private static final Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    public HomeController(Environment environment){
        appMode = environment.getProperty("app-mode");
    }

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Szymon");
        model.addAttribute("mode", appMode);

        return "index";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        Iterable<HouseHold> users = repositoryH.findAll();
        model.addAttribute("users", users);

        for (HouseHold user : users) {
            logger.info("List object: " + new Gson().toJson(user));
        }
        return "home";
    }


    @RequestMapping("/random")
    public RedirectView random() {
        HouseHold houseHold = new HouseHold();
        houseHold.setName("test name" + new Random().nextInt(1000));
        houseHold = repositoryH.save(houseHold);
        logger.info("Saved object: " + new Gson().toJson(houseHold));
        logger.info("Saved object id: " + houseHold.getId());

        return new RedirectView("/home");
    }
}
