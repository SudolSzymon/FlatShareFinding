package com.szymon.ffproject.controller;

import com.google.common.collect.Sets;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.FieldUtil;
import com.szymon.ffproject.util.annotation.InputType;
import java.security.Principal;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/house")
public class HouseholdController extends GenericController {


    public HouseholdController(HouseholdService householdService, UserService userService) {
        super(householdService, userService);
    }

    @GetMapping("/view")
    public String login(Model model, Principal principal) {
        Household house = serviceU.getHousehold(principal);
        model.addAttribute("members", house.getMembers());
        model.addAttribute("houseName", house.getName());
        boolean isAdmin = serviceU.isHouseAdmin(principal);
        FieldUtil.addList(model, house.getMembers().stream().map(serviceU::getUser).collect(Collectors.toSet()), "Members",
                          isAdmin ? "/house/remove" : null, null);
        if (isAdmin) {
            FieldUtil.addObject(model, house, "/house/edit", "Edit house");
            return "generic/genericListFormOnSide";
        }
        return "generic/genericList";
    }

    @PostMapping(value = "/edit")
    public String edit(Principal principal, @Valid @ModelAttribute("object") Household house,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(house, bindingResult, redirectAttributes);
            return "redirect:/house/view";
        }
        if (!serviceU.isHouseAdmin(principal))
            throw unAuthorisedException("not authorised to modify  household");
        Household oldHouse = serviceU.getHousehold(principal);
        serviceH.updateHouse(house, oldHouse);
        return "redirect:/house/view";
    }


    @GetMapping("/add")
    public String add(Model model, Household house) {
        FieldUtil.addObject(model, house, "/house/create", "Create Household");
        return "generic/genericForm";
    }

    @PostMapping(value = "/create")
    public String create(@ModelAttribute Household household, Principal principal) {
        if (serviceH.exist(household.getName()))
            return "error/duplicate";
        serviceH.newHousehold(household, Sets.newHashSet(principal.getName()));
        serviceU.assignHouse(household.getName(), serviceU.getUser(principal), true);

        return "redirect:/house/view";
    }


    @GetMapping(value = "/register")
    public String prepareRegister(Model model, HouseholdSignUpData data) {
        FieldUtil.addObject(model, data, "/house/register", "Register to HouseHold");
        return "houseRegistration";
    }


    @PostMapping(value = "/register")
    public RedirectView register(@ModelAttribute HouseholdSignUpData householdSignUpData, Principal principal) {
        if (serviceH.verifyPassword(householdSignUpData.getName(), householdSignUpData.getPass())) {
            serviceH.addMember(householdSignUpData.getName(), principal);
            serviceU.assignHouse(householdSignUpData.getName(), serviceU.getUser(principal), false);
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
