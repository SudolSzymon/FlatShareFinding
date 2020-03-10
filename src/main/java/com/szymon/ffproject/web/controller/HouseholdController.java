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
import java.util.stream.Collectors;
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
        User user = getUser(principal);
        model.addAttribute("members", house.getMembers());
        model.addAttribute("houseName", house.getName());
        boolean isAdmin = GenericController.isHouseAdmin(user);
        FieldUtil.addList(model, house.getMembers().stream().map(this::getUser).collect(Collectors.toSet()), "Members",
                          isAdmin ? "/house/remove" : null, null);
        if (isAdmin) {
            FieldUtil.addForm(model, house, "/house/edit", "Edit house");
            return "generic/genericListFormOnSide";
        }
        return "generic/genericList";
    }


    @GetMapping("/add")
    public String add(Model model, Household house) {
        FieldUtil.addForm(model, house, "/house/create", "Create Household");
        return "generic/genericForm";
    }

    @PostMapping(value = "/create")
    public String create(@ModelAttribute Household household, Principal principal) {
        if (repositoryH.existsById(household.getName()))
            return "error/duplicate";
        household.encrypt(passwordEncoder);
        household.setMembers(Sets.newHashSet(principal.getName()));
        User user = getUser(principal);
        user.getRoles().add(GenericController.HOUSE_ADMIN_PREFIX + household.getName());
        user.setHouseName(household.getName());
        repositoryU.save(user);
        repositoryH.save(household);
        return "redirect:/house/view";
    }


    @GetMapping(value = "/register")
    public String prepareRegister(Model model, HouseholdSignUpData data) {
        FieldUtil.addForm(model, data, "/house/register", "Register to HouseHold");
        return "houseRegistration";
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
