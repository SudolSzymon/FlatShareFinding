package com.szymon.ffproject.service;

import static com.szymon.ffproject.controller.GenericController.badRequestException;
import static com.szymon.ffproject.service.HouseholdService.HOUSE_ADMIN_PREFIX;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.szymon.ffproject.controller.GenericController;
import com.szymon.ffproject.dao.S3DAO;
import com.szymon.ffproject.dao.UserDAO;
import com.szymon.ffproject.database.entity.Event;
import com.szymon.ffproject.database.entity.FileEntity;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.util.EntityUtil;
import com.szymon.ffproject.util.UserFilter;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final Set<String> ALLOWED_PHOTO_FORMATS = Sets.newHashSet("jpg", "png", "jpeg");
    private final UserDAO userDAO;
    private final HouseholdService serviceH;
    private final S3DAO s3DAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, HouseholdService serviceH, S3DAO s3DAO,
                       PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.serviceH = serviceH;
        this.s3DAO = s3DAO;
        this.passwordEncoder = passwordEncoder;
    }


    public Household getHousehold(Principal principal) {
        User user = getUser(principal);
        String houseName = user.getHouseName();
        if (houseName == null)
            throw GenericController.redirectException("/user/house");
        Household house = serviceH.getHousehold(houseName);
        if (house == null) {
            user.setHouseName(null);
            userDAO.save(user);
            throw GenericController.redirectException("/user/house");
        }
        return house;
    }

    public User getUser(Principal principal) {
        return getUser(principal.getName());
    }

    public User getUser(String name) {
        User user = userDAO.get(name);
        if (user != null)
            return user;
        throw badRequestException("User not found");
    }


    public boolean isHouseAdmin(Principal principal) {
        User user = getUser(principal);
        return user.getRoles().contains(HOUSE_ADMIN_PREFIX + user.getHouseName());
    }


    public void assignHouse(String name, User user, boolean isAdmin) {
        if (isAdmin) user.getRoles().add(HOUSE_ADMIN_PREFIX + name);
        user.setHouseName(name);
        userDAO.save(user);
    }

    public boolean exists(User user) {
        return userDAO.exist(user.getName());
    }

    public void newUser(User user) {
        user.encrypt(passwordEncoder).setRoles(Collections.singletonList("user"));
        userDAO.save(user);
    }

    public void updateUser(Principal principal, User user) {
        User oldUser = getUser(principal);
        EntityUtil.update(user, oldUser);
        userDAO.save(user);
    }

    public void addImage(Principal principal, MultipartFile image) {
        User user = getUser(principal);
        if (image.getOriginalFilename() != null && ALLOWED_PHOTO_FORMATS
            .contains(Files.getFileExtension(image.getOriginalFilename()))) {
            String id = user.getAvatarUniqueName();
            if (id != null)
                s3DAO.delete(id);
            FileEntity file = null;
            try {
                file = new FileEntity(image);
                s3DAO.save(file);
                user.setAvatarUniqueName(file.getEntityID());
            } catch (IOException e) {
                LOGGER.error("Unable to turn image into file entity", e);
                user.setAvatarUniqueName(null);
                throw badRequestException("Unable to turn image into file entity");
            }
        }
        userDAO.save(user);
    }

    public void leaveHouse(Principal principal) {
        User user = getUser(principal);
        Household house = getHousehold(principal);
        boolean houseAdmin = isHouseAdmin(principal);
        if (houseAdmin) {
            house.getMembers().forEach(name -> assignHouse(null, getUser(name), false));
            serviceH.delete(house);
        } else {
            assignHouse(null, user, false);
            house.getMembers().removeIf(n -> n.equals(user.getName()));
        }
    }

    public List<User> getUsers(UserFilter filter) {
        return userDAO.get(filter);
    }

    public void addEvent(Event event, Principal principal) {
        if (event.isRepeat())
            event.convertToRecurringEvent();
        Household household = getHousehold(principal);
        Set<String> members = household.getMembers();
        event.getParticipants().stream().filter(members::contains).map(this::getUser).forEach(user -> {
            user.getCalendar().addEvent(event);
            userDAO.save(user);
        });
    }

    public void deleteEvent(Principal principal, String id) {
        User user = getUser(principal);
        user.getCalendar().getEvents().remove(id);
    }

    public String getUserImageURL(User user) {
        if (user.getAvatarUniqueName() != null)
            return s3DAO.getS3Link(user.getAvatarUniqueName());
        return null;
    }


    public Collection<Event> getAllEvents(Household household) {
        Set<Event> events = new HashSet<>();
        household.getMembers().stream().map(u -> getUser(u).getCalendar().getEvents().values()).forEach(events::addAll);
        return events;
    }
}
