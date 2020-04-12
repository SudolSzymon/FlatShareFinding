package com.szymon.ffproject.controller;

import com.szymon.ffproject.cache.UserFilter;
import com.szymon.ffproject.dao.S3DAO;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.Amenity;
import com.szymon.ffproject.util.FieldUtil;
import com.szymon.ffproject.util.HouseType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search")
public class SearchController extends GenericController {


    public SearchController(HouseholdService householdService,
                            UserService userService,
                            S3DAO s3DAO) {
        super(householdService, userService, s3DAO);
    }

    @RequestMapping()
    public String search(Model model, @ModelAttribute UserFilter filter) {
        List<User> res = serviceU.getUsers(filter);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("amenities", Amenity.stringValues());
        values.put("houseTypes", HouseType.stringValues());
        FieldUtil.addObject(model, filter, "/search", "Search", values);
        FieldUtil.addList(model, res, "Best match for you", null, null, "/user/view");
        return "searchResults";
    }
}
