package com.szymon.ffproject.web.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.Event;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.config.CalendarConfig;
import com.szymon.ffproject.web.util.FormUtil;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/calendar")
public class CalendarController extends GenericController {


    @Qualifier("calendarConverter")
    @Autowired
    private Gson jsonConverter;

    private final ObjectProvider<CalendarConfig> configProvider;

    public CalendarController(UserRepository repository, ObjectProvider<CalendarConfig> configProvider,
                              HouseholdRepository repositoryH) {
        super(repository, repositoryH);
        this.configProvider = configProvider;
    }


    @GetMapping("/{username}")
    public String view(Model model, @PathVariable String username) {
        Optional<User> user = repositoryU.findById(username);
        if (user.isPresent()) {
            CalendarConfig config = configProvider.getObject();
            config.setEvents(user.get().getCalendar().getEvents());
            model.addAttribute("calendarConfig", jsonConverter.toJson(config));
        } else throw badRequestException("user  does not  exist");
        return "calendar";
    }

    @GetMapping("/full")
    public String viewAll(Model model, Principal principal) {
        Household household = getHousehold(principal);
        CalendarConfig config = configProvider.getObject();
        List<Event> eventList = new LinkedList<>();
        household.getMembers().stream().map(u -> repositoryU.findById(u).get().getCalendar().getEvents())
            .forEach(eventList::addAll);
        eventList = eventList.stream().distinct().collect(Collectors.toList());
        config.setEvents(eventList);
        model.addAttribute("calendarConfig", jsonConverter.toJson(config));
        return "calendar";
    }


    @GetMapping("/event/add")
    public String add(Model model) {
        Event event = getAttribute(model, "object", Event.class);
        FormUtil.addForm(model, event, "/calendar/event/create", "Create Event");
        return "add";
    }

    @PostMapping(value = "/event/create")
    public String create(Principal principal, @Valid @ModelAttribute("object") Event object,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(object, bindingResult, redirectAttributes);
            return "redirect:/calendar/event/add";
        }
        Household household = getHousehold(principal);
        Set<String> members = household.getMembers();
        object.getParticipants().stream().filter(members::contains).map(p -> repositoryU.findById(p).get()).forEach(user -> {
            user.getCalendar().addEvent(object);
            repositoryU.save(user);
        });
        return "redirect:/user/house";
    }
}
