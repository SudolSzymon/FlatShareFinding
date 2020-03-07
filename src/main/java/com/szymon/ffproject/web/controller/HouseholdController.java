package com.szymon.ffproject.web.controller;

import com.google.common.collect.Sets;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.FieldUtil;
import com.szymon.ffproject.web.util.annotation.InputType;
import java.security.Principal;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/house")
public class HouseholdController extends GenericController {

    private final PasswordEncoder passwordEncoder;

    public HouseholdController(HouseholdRepository repository, PasswordEncoder passwordEncoder,
                               UserRepository repositoryU) {
        super(repositoryU, repository);
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/view")
    public String login(Model model, Principal principal) {
        Household house = getHousehold(principal);
        model.addAttribute("members", house.getMembers());
        model.addAttribute("houseName", house.getName());
        return "household";
    }


    @GetMapping("/add")
    public String add(Model model, Household house) {
        FieldUtil.addForm(model, house, "/house/create", "Create Household");
        return "genericForm";
    }

    @PostMapping(value = "/create")
    public RedirectView create(@ModelAttribute Household household, Principal principal) {
        household.encrypt(passwordEncoder);
        household.setMembers(Sets.newHashSet(principal.getName()));
        User user = getUser(principal);
        user.getRoles().add("HAdmin");
        user.setHouseName(household.getName());
        repositoryU.save(user);
        repositoryH.save(household);
        return new RedirectView("/house/view");
    }


    @GetMapping(value = "/register")
    public String prepareRegister(Model model, HouseholdSignUpData data) {
        FieldUtil.addForm(model, data, "/house/register", "Register to HouseHold");
        return "genericForm";
    }


    @PostMapping(value = "/register")
    public RedirectView register(@ModelAttribute HouseholdSignUpData householdSignUpData, Principal principal) {
        Household house = getHousehold(householdSignUpData.name);
        User user = getUser(principal);
        if (passwordEncoder.matches(householdSignUpData.getPass(), house.getPassword())) {
            Set<String> members = house.getMembers();
            members.add(principal.getName());
            repositoryH.save(house);
            user.setHouseName(house.getName());
            repositoryU.save(user);
            return new RedirectView("/house/view");
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong household password or name");

    }

    private static class HouseholdSignUpData {

        private String name;
        @InputType(type = "pass")
        private String pass;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }


}
