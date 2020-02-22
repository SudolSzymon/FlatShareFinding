package com.szymon.ffproject.web.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.entity.UserCalendar;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.EntityUtil;
import com.szymon.ffproject.web.util.FormUtil;
import java.security.Principal;
import java.util.Optional;
import org.apache.log4j.Logger;
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
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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
        user.encrypt(passwordEncoder).setRoles("admin", "user").setCalendar(new UserCalendar());
        repository.save(user);
        return new RedirectView("/");
    }

    @PostMapping(value = "/save")
    public RedirectView save(@ModelAttribute User user,Principal principal) throws IllegalAccessException {
        Optional<User> oldUser = repository.findById(principal.getName());
        EntityUtil.update(user, oldUser.get());
        logger.info("Created user: " + new Gson().toJson(user.encrypt(passwordEncoder).setRoles("admin", "user")));
        repository.save(user);
        return new RedirectView("/");
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String currentUserName(Model model, Principal principal) {
        Optional<User> user = repository.findById(principal.getName());
        FormUtil.addForm(model, user.get(), "/user/save", "Profile");
        return "userProfile";
    }

}
