package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserDataCacheFilter;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.Amenity;
import com.szymon.ffproject.web.util.FieldUtil;
import com.szymon.ffproject.web.util.HouseType;
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

    public SearchController(UserRepository repositoryU,
                            HouseholdRepository repositoryH) {
        super(repositoryU, repositoryH);
    }

    @RequestMapping()
    public String search(Model model, @ModelAttribute UserDataCacheFilter filter) {
        List<User> res = dataCache.getUsers(filter);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("amenities", Amenity.stringValues());
        values.put("houseTypes", HouseType.stringValues());
        FieldUtil.addObject(model, filter, "/search", "Search", values);
        FieldUtil.addList(model, res, "Best match for you", null, null, "/user/view");
        return "searchResults";
    }
}
