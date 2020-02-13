package com.szymon.ffproject.web.controllers;

import com.szymon.ffproject.database.entity.AuthenticationUser;
import com.szymon.ffproject.database.repository.AuthenticationUserRepository;
import com.szymon.ffproject.server.CreateUserData;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SecurityController {

    private static final Logger logger = Logger.getLogger(SecurityController.class);

    private final AuthenticationUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public SecurityController(AuthenticationUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping("/logout")
    public RedirectView logout() {
        return new RedirectView("/");
    }

    @GetMapping("/register")
    public String register(Model model, CreateUserData userData) {
        model.addAttribute("userData", userData);
        return "register";
    }

    @PostMapping(value="/create")
    public RedirectView create(@ModelAttribute CreateUserData userData){
        repository.save(userData.buildAuthUser(passwordEncoder).setRoles("admin", "user"));
        logger.info("Created user: " + userData.getUser());
        return new RedirectView("/");
    }



}
