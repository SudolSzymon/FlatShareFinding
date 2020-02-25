package com.szymon.ffproject.web.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.entity.UserCalendar;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.EntityUtil;
import com.szymon.ffproject.web.util.FormUtil;
import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/user")
public class UserController extends GenericController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, HouseholdRepository repositoryH) {
        super(repository, repositoryH);
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model, User user) {
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/create")
    public RedirectView create(@ModelAttribute User user) {
        logger.info("Created user: " + new Gson()
            .toJson(user.encrypt(passwordEncoder).setRoles("admin", "user").setCalendar(new UserCalendar())));
        repositoryU.save(user);
        return new RedirectView("/");
    }

    @PostMapping(value = "/save")
    public RedirectView save(@ModelAttribute User user, Principal principal) {
        Optional<User> oldUser = repositoryU.findById(principal.getName());
        EntityUtil.update(user, oldUser.get());
        logger.info("Created user: " + new Gson().toJson(user.encrypt(passwordEncoder).setRoles("admin", "user")));
        repositoryU.save(user);
        return new RedirectView("/");
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String currentUserName(Model model, Principal principal) {
        User user = repositoryU.findById(principal.getName()).get();
        FormUtil.addForm(model, user, "/user/save", "Profile");
        return "userProfile";
    }

    @RequestMapping(value = "/house", method = RequestMethod.GET)
    public RedirectView house(Model model, Principal principal) {
        User user = repositoryU.findById(principal.getName()).get();
        String houseName = user.getHouseName();
        if (houseName != null && !houseName.isEmpty())
            return new RedirectView("/house/" + houseName);
        else
            return new RedirectView("/house/register");
    }

}
