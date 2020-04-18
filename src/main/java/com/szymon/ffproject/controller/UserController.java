package com.szymon.ffproject.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.Amenity;
import com.szymon.ffproject.util.FieldUtil;
import com.szymon.ffproject.util.Gender;
import com.szymon.ffproject.util.HouseType;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/user")
public class UserController extends GenericController {

    private final Gson gson;

    public UserController(HouseholdService householdService,
                          UserService userService, Gson gson) {
        super(householdService, userService);
        this.gson = gson;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model, User user) {
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/create")
    public String create(@ModelAttribute User user) {
        if (serviceU.exists(user))
            return "error/duplicate";
        serviceU.newUser(user);
        logger.info("Created user: " + gson.toJson(user));
        return "redirect:/";
    }

    @PostMapping(value = "/save")
    public String save(Principal principal, @Valid @ModelAttribute("object") User user,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(user, bindingResult, redirectAttributes);
            return "redirect:/user/profile";
        }
        serviceU.updateUser(principal, user);
        return "redirect:/";
    }

    @RequestMapping(value = "/uploadAvatar")
    public String upload(Principal principal, @RequestParam MultipartFile image) throws IOException {
        serviceU.addImage(principal, image);
        return "redirect:/user/profile";
    }


    @GetMapping(value = "/profile")
    public String currentUserName(Model model, Principal principal) {
        User user = getAttribute(model, "object", User.class, false);
        if (user == null)
            user = serviceU.getUser(principal);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("amenities", Amenity.stringValues());
        values.put("houseTypes", HouseType.stringValues());
        values.put("gender", Gender.stringValues());
        FieldUtil.addObject(model, user, "/user/save", "Profile", values);
        model.addAttribute("imageUrl", serviceU.getUserImageURL(user));
        return "profile";
    }

    @GetMapping(value = "/view/{user}")
    public String viewUser(Model model, @PathVariable String user) {
        User dbUser = serviceU.getUser(user);
        FieldUtil.addObject(model, dbUser, null, null, null);
        model.addAttribute("imageUrl", serviceU.getUserImageURL(dbUser));
        return "profileView";
    }

    @RequestMapping(value = "/house")
    public RedirectView house(Principal principal) {
        User user = serviceU.getUser(principal);
        String houseName = user.getHouseName();
        if (houseName != null && !houseName.isEmpty())
            return new RedirectView("/house/view");
        else
            return new RedirectView("/house/register");
    }

    @RequestMapping(value = "/leave")
    public String leave(Principal principal) {
        serviceU.leaveHouse(principal);
        return "redirect:/user/house";
    }

}
