package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.web.util.FormUtil;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/house")
public class HouseholdController {

    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = Logger.getLogger(HouseholdController.class);

    private final HouseholdRepository repository;

    public HouseholdController(HouseholdRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{name}")
    public String login(@PathVariable String name, Model model) {
        Optional<Household> household = repository.findById(name);
        List<String> members = household.isPresent() ? household.get().getMembers() : new ArrayList<>();
        model.addAttribute("members", members);
        return "household";
    }


    @GetMapping("/add")
    public String add(Model model, Household house) {
        FormUtil.addForm(model, house, "/house/create", "Create Household");
        return "addHousehold";
    }

    @PostMapping(value = "/create")
    public RedirectView create(@ModelAttribute Household household) {
        household.encrypt(passwordEncoder);
        repository.save(household);
        return new RedirectView("/house/" + household.getName());
    }


}
