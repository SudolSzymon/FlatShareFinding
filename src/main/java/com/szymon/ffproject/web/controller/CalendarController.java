package com.szymon.ffproject.web.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.Event;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.config.CalendarConfig;
import com.szymon.ffproject.web.util.FieldUtil;
import java.security.Principal;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/calendar")
public class CalendarController extends GenericController {


    private final Gson jsonConverter;

    private final ObjectProvider<CalendarConfig> configProvider;

    public CalendarController(UserRepository repositoryU, ObjectProvider<CalendarConfig> configProvider,
                              HouseholdRepository repositoryH, @Qualifier("calendarConverter") Gson jsonConverter) {
        super(repositoryU, repositoryH);
        this.configProvider = configProvider;
        this.jsonConverter = jsonConverter;
    }


    @GetMapping("/view")
    public String view(Model model, Principal principal) {
        User user = getUser(principal);

        CalendarConfig config = configProvider.getObject();
        config.setEvents(user.getCalendar().getEvents());
        model.addAttribute("calendarConfig", jsonConverter.toJson(config));
        Set<Event> eventList = new HashSet<>();
        Household household = getHousehold(principal);
        household.getMembers().stream().map(u -> getUser(u).getCalendar().getEvents()).forEach(eventList::addAll);
        config.setEvents(eventList);
        model.addAttribute("fullCalendarConfig", jsonConverter.toJson(config));
        return "calendar";
    }


    @GetMapping("/event/manage")
    public String manage(Model model, Principal principal) {
        User user = getUser(principal);
        Event event = getAttribute(model, "object", Event.class);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("participants", getHousehold(principal).getMembers());
        values.put("repeatOn", Arrays.stream(DayOfWeek.values()).map(Enum::toString).collect(Collectors.toCollection(LinkedHashSet::new)));
        FieldUtil.addForm(model, event, "/calendar/event/create", "Create Event", values);
        FieldUtil.addList(model, user.getCalendar().getEvents(), "Events", null, null);
        return "generic/genericListFormOnSide";
    }

    @PostMapping(value = "/event/create")
    public String create(Principal principal, @Valid @ModelAttribute("object") Event object,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(object, bindingResult, redirectAttributes);
            return "redirect:/calendar/event/manage";
        }
        if (object.isRepeat())
            object.convertToRecurringEvent();
        Household household = getHousehold(principal);
        Set<String> members = household.getMembers();
        object.getParticipants().stream().filter(members::contains).map(this::getUser).forEach(user -> {
            user.getCalendar().addEvent(object);
            repositoryU.save(user);
        });
        return "redirect:/calendar/event/manage";
    }
}
