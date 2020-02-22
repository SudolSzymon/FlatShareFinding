package com.szymon.ffproject.web.controller;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.Event;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.config.CalendarConfig;
import com.szymon.ffproject.web.util.FormUtil;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
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

@Controller
@RequestMapping(value = "/calendar")
public class CalendarController{

    private static final Logger logger = Logger.getLogger(CalendarController.class);

    @Qualifier("calendarConverter")
    @Autowired
    private Gson jsonConverter;

    private final ObjectProvider<CalendarConfig> configProvider;

    private final UserRepository repository;

    public CalendarController(UserRepository repository, ObjectProvider<CalendarConfig> configProvider) {
        this.repository = repository;
        this.configProvider = configProvider;
    }


    @GetMapping("/{username}")
    public String test(Model model, @PathVariable String username) {
        Optional<User> user = repository.findById(username);
        user.ifPresent(u -> {
            CalendarConfig config = configProvider.getObject();
            config.setEvents(u.getCalendar().getEvents());
            model.addAttribute("calendarConfig", jsonConverter.toJson(config));
        });

        return "calendar";
    }

    @GetMapping("/event/add")
    public String add(Model model, Event event) {
        FormUtil.addForm(model, event, "/calendar/event/create", "Create Event");
        return "addEvent";
    }

    @PostMapping(value = "/event/create")
    public String create(HttpServletRequest request, @ModelAttribute Event event) {
        event.getParticipants().stream().map(p -> repository.findById(p).get()).forEach(user -> {
            user.getCalendar().addEvent(event);
            repository.save(user);
        });
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
