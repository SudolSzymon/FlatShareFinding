package com.szymon.ffproject.web.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.Amenity;
import com.szymon.ffproject.web.util.EntityUtil;
import com.szymon.ffproject.web.util.FieldUtil;
import com.szymon.ffproject.web.util.HouseType;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        user.encrypt(passwordEncoder).setRoles(Collections.singletonList("user"));
        repositoryU.save(user);
        logger.info("Created user: " + new Gson().toJson(user));
        return new RedirectView("/");
    }

    @PostMapping(value = "/save")
    public String save(Principal principal, @Valid @ModelAttribute("object") User user,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(user, bindingResult, redirectAttributes);
            return "redirect:/user/profile";
        }
        User oldUser = getUser(principal);
        EntityUtil.update(user, oldUser);
        repositoryU.save(user);
        return "redirect:/";
    }


    @GetMapping(value = "/profile")
    public String currentUserName(Model model, Principal principal) {
        User user = getAttribute(model, "object", User.class, false);
        if (user == null)
            user = getUser(principal);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("amenities", Arrays.stream(Amenity.values()).map(Amenity::toString).collect(Collectors.toSet()));
        values.put("houseTypes", Arrays.stream(HouseType.values()).map(HouseType::toString).collect(Collectors.toSet()));
        FieldUtil.addForm(model, user, "/user/save", "Profile", values);
        return "genericForm";
    }

    @RequestMapping(value = "/house")
    public RedirectView house(Principal principal) {
        User user = getUser(principal);
        String houseName = user.getHouseName();
        if (houseName != null && !houseName.isEmpty())
            return new RedirectView("/house/view");
        else
            return new RedirectView("/house/register");
    }

}
