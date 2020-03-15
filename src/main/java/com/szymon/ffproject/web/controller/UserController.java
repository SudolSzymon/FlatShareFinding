package com.szymon.ffproject.web.controller;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.Amenity;
import com.szymon.ffproject.web.util.EntityUtil;
import com.szymon.ffproject.web.util.FieldUtil;
import com.szymon.ffproject.web.util.HouseType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/user")
public class UserController extends GenericController {

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, HouseholdRepository repositoryH, PasswordEncoder passwordEncoder) {
        super(repository, repositoryH);
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model, User user) {
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/create")
    public String create(@ModelAttribute User user) {
        user.encrypt(passwordEncoder).setRoles(Collections.singletonList("user"));
        if (repositoryU.existsById(user.getName()))
            return "duplicate";
        repositoryU.save(user);
        logger.info("Created user: " + new Gson().toJson(user));
        return "redirect:/";
    }

    @PostMapping(value = "/save")
    public String save(Principal principal, @Valid @ModelAttribute("object") User user,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            storeBindingResult(user, bindingResult, redirectAttributes);
            return "redirect:/user/profile";
        }
        User oldUser = getUser(principal);
        EntityUtil.update(user, oldUser);
        repositoryU.save(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/uploadAvatar")
    public String hgy(Principal principal, @RequestParam MultipartFile image) throws IOException {

        User user = getUser(principal);
        if (image.getOriginalFilename() != null && Files.getFileExtension(image.getOriginalFilename()).equals("jpg")) {
            String id = user.getAvatarUniqueName();
            if (id != null)
                deleteFromS3(id);
            File file = new File(image.getOriginalFilename());
            file.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(image.getBytes());
                user.setAvatarUniqueName(putToS3(file));
            } catch (IOException e) {
                logger.error("Can't upload to S3", e);
                throw e;
            } finally {
                file.delete();
            }
        }
        repositoryU.save(user);
        return "redirect:/user/profile";
    }


    @GetMapping(value = "/profile")
    public String currentUserName(Model model, Principal principal) {
        User user = getAttribute(model, "object", User.class, false);
        if (user == null)
            user = getUser(principal);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("amenities", Arrays.stream(Amenity.values()).map(Amenity::toString).collect(Collectors.toSet()));
        values.put("houseTypes", Arrays.stream(HouseType.values()).map(HouseType::toString).collect(Collectors.toSet()));
        FieldUtil.addObject(model, user, "/user/save", "Profile", values);
        if (user.getAvatarUniqueName() != null)
            model.addAttribute("imageUrl", getS3Link(user.getAvatarUniqueName()));
        return "profile";
    }

    @GetMapping(value = "/view/{user}")
    public String viewUser(Model model, @PathVariable String user) {
        User dbUser = getUser(user);
        FieldUtil.addObject(model, dbUser, null, null, null);
        return "profileView";
    }

    @RequestMapping(value = "/house")
    public RedirectView house(Principal principal) {
        User user = getUser(principal);
        String houseName = user.getHouseName();
        if (houseName != null && !houseName.isEmpty())
            return new RedirectView("/house/view");
        else
            return new RedirectView("/house/register");
    }

    @RequestMapping(value = "/leave")
    public String leave(Principal principal) {
        User user = getUser(principal);
        Household house = getHousehold(principal);
        boolean houseAdmin = GenericController.isHouseAdmin(user);
        user.setHouseName(null);
        house.getMembers().removeIf(n -> n.equals(user.getName()));
        repositoryU.save(user);
        if (houseAdmin) {
            house.getMembers().forEach(name -> getUser(name).setHouseName(null));
            repositoryH.delete(house);
        }
        return "redirect:/user/house";
    }

}
