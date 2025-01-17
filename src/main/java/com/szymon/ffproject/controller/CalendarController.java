package com.szymon.ffproject.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.config.CalendarConfig;
import com.szymon.ffproject.database.entity.Event;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.FieldUtil;
import java.security.Principal;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/calendar")
public class CalendarController extends GenericController {


    private final Gson calendarConverter;

    private final ObjectProvider<CalendarConfig> configProvider;

    public CalendarController(HouseholdService householdService,
                              UserService userService, @Qualifier(value = "calendarConverter") Gson calendarConverter,
                              ObjectProvider<CalendarConfig> configProvider) {
        super(householdService, userService);
        this.calendarConverter = calendarConverter;
        this.configProvider = configProvider;
    }


    @GetMapping("/view")
    public String view(Model model, Principal principal) {
        User user = serviceU.getUser(principal);

        CalendarConfig config = configProvider.getObject();
        config.setEvents(user.getCalendar().getEvents().values());
        model.addAttribute("calendarConfig", calendarConverter.toJson(config));
        Household household = serviceU.getHousehold(principal);
        Collection<Event> eventList = serviceU.getAllEvents(household);
        config.setEvents(eventList);
        model.addAttribute("fullCalendarConfig", calendarConverter.toJson(config));
        return "calendar";
    }


    @GetMapping("/event/manage")
    public String manage(Model model, Principal principal) {
        User user = serviceU.getUser(principal);
        Event event = getAttribute(model, "object", Event.class);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("participants", serviceU.getHousehold(principal).getMembers());
        values.put("repeatOn",
                   Arrays.stream(DayOfWeek.values()).map(Enum::toString).collect(Collectors.toCollection(LinkedHashSet::new)));
        FieldUtil.addObject(model, event, "/calendar/event/create", "Create Event", values);
        FieldUtil.addList(model, user.getCalendar().getEvents().values(), "Events", "../event/delete", null);
        return "generic/genericListFormOnSide";
    }

    @PostMapping(value = "/event/create")
    public String create(Principal principal, @Valid @ModelAttribute("object") Event event,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(event, bindingResult, redirectAttributes);
            return "redirect:/calendar/event/manage";
        }
        serviceU.addEvent(event, principal);
        return "redirect:/calendar/event/manage";
    }

    @RequestMapping(value = "/event/delete/{id}")
    public String delete(Principal principal, @PathVariable String id) {
        serviceU.deleteEvent(principal, id);
        return "redirect:/calendar/event/manage";
    }
}
