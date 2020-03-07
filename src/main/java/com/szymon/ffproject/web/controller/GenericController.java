package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.DataCache;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;
import javax.validation.Valid;
import javax.ws.rs.RedirectionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public abstract class GenericController {

    protected static final Logger logger = Logger.getLogger(GenericController.class);

    @Autowired
    protected DataCache dataCache;
    protected final UserRepository repositoryU;
    protected final HouseholdRepository repositoryH;

    public GenericController(
        UserRepository repositoryU, HouseholdRepository repositoryH) {
        this.repositoryU = repositoryU;
        this.repositoryH = repositoryH;
    }

    public static ResponseStatusException badRequestException(String s) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, s);
    }

    public static ResponseStatusException unAuthorisedException(String s) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, s);
    }

    public static RedirectionException redirectException(String url) {
        return new RedirectionException(302, URI.create(url));
    }

    public Household getHousehold(Principal principal) {
        User mainUser = getUser(principal);
        String houseName = mainUser.getHouseName();
        if (houseName == null)
            throw redirectException("/user/house");
        Optional<Household> house = repositoryH.findById(houseName);
        if (house.isPresent())
            return house.get();
        throw badRequestException("House not found");
    }

    public Household getHousehold(String name) {
        Optional<Household> house = repositoryH.findById(name);
        if (house.isPresent())
            return house.get();
        throw badRequestException("House not found");
    }


    public User getUser(Principal principal) {
        Optional<User> user = repositoryU.findById(principal.getName());
        if (user.isPresent())
            return user.get();
        throw badRequestException("User not found");
    }

    public <T> T getAttribute(Model model, String name, Class<T> type) {return getAttribute(model, name, type, true);}

    public <T> T getAttribute(
        Model model, String name, Class<T> type, boolean init) {
        T expense = null;
        if (model.containsAttribute(name))
            expense = (T) model.getAttribute(name);
        else if (init) {
            try {
                expense = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
        return expense;
    }

    public void storeBindingResult(
        @ModelAttribute("object") @Valid Object object,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.object", bindingResult);
        redirectAttributes.addFlashAttribute("object", object);
    }

    @ExceptionHandler(RedirectionException.class)
    public RedirectView handleRedirectionException(RedirectionException ex) {
        return new RedirectView(ex.getLocation().toString());
    }
}
