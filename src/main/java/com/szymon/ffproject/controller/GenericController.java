package com.szymon.ffproject.controller;

import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import javax.ws.rs.RedirectionException;
import org.apache.log4j.Logger;
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
    protected final HouseholdService serviceH;
    protected final UserService serviceU;

    public GenericController(HouseholdService householdService, UserService userService) {

        serviceH = householdService;
        serviceU = userService;
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


    protected <T> T getAttribute(Model model, String name, Class<T> type) {return getAttribute(model, name, type, true);}

    protected <T> T getAttribute(
        Model model, String name, Class<T> type, boolean init) {
        T attribute = null;
        if (model.containsAttribute(name))
            attribute = (T) model.getAttribute(name);
        else if (init) {
            try {
                attribute = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
        return attribute;
    }

    protected void storeBindingResult(
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
