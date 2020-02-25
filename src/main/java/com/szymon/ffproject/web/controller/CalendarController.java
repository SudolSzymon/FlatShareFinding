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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/full/{house}")
    public String viewAll(Model model, @PathVariable String house) {
        Optional<Household> household = repositoryH.findById(house);
        if (household.isPresent()) {
            CalendarConfig config = configProvider.getObject();
            List<Event> eventList = new LinkedList<>();
            household.get().getMembers().stream().map(u -> repositoryU.findById(u).get().getCalendar().getEvents())
                .forEach(eventList::addAll);
            eventList = eventList.stream().distinct().collect(Collectors.toList());
            config.setEvents(eventList);
            model.addAttribute("calendarConfig", jsonConverter.toJson(config));
        } else throw badRequestException("household  does not  exist");
        return "calendar";
    }


    @GetMapping("/event/add")
    public String add(Model model, Event event) {
        FormUtil.addForm(model, event, "/calendar/event/create", "Create Event");
        return "add";
    }

    @PostMapping(value = "/event/create")
    public RedirectView create(Principal principal, @ModelAttribute Event event) {
        User mainUser = repositoryU.findById(principal.getName()).get();
        Household household = repositoryH.findById(mainUser.getHouseName()).get();
        Set<String> members = household.getMembers();
        event.getParticipants().stream().filter(members::contains).map(p -> repositoryU.findById(p).get()).forEach(user -> {
            user.getCalendar().addEvent(event);
            repositoryU.save(user);
        });
        return new RedirectView("/house/" + household.getName());
    }
}
