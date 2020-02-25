package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.ShopItem;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.StringWrapper;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/shop")
public class ShoppingListController extends GenericController {

    public ShoppingListController(UserRepository repositoryU, HouseholdRepository repositoryH) {
        super(repositoryU, repositoryH);
    }

    @ModelAttribute("shopLists")
    public Map<String, List<ShopItem>> setUpList() {
        return new HashMap<>();
    }

    @GetMapping("/view/all")
    public String view(Model model, Principal principal) {
        Household household = getHousehold(principal);
        model.addAttribute("map", household.getLists());
        model.addAttribute("houseName", household.getName());
        model.addAttribute("stringWrap", new StringWrapper());
        return "shopLists";
    }

    @PostMapping("/create")
    public RedirectView create(Principal principal, @ModelAttribute StringWrapper stringWrapper) {
        Household household = getHousehold(principal);
        Map<String, List<ShopItem>> shopLists = household.getLists();
        shopLists.put(stringWrapper.getValue(), new ArrayList<>());
        repositoryH.save(household);
        return new RedirectView("/shop/edit/" + stringWrapper.getValue());
    }

    @GetMapping("/edit/{name}")
    public String edit(Model model, @PathVariable String name, Principal principal) {
        Household household = getHousehold(principal);
        Map<String, List<ShopItem>> shopLists = household.getLists();
        model.addAttribute("listName", name);
        model.addAttribute("list", shopLists.get(name));
        model.addAttribute("item", new ShopItem());
        return "listEdit";
    }

    @GetMapping("/delete/{name}")
    public RedirectView delete(@PathVariable String name, Principal principal) {
        Household household = getHousehold(principal);
        Map<String, List<ShopItem>> shopLists = household.getLists();
        shopLists.remove(name);
        repositoryH.save(household);
        return new RedirectView("/shop/view/all");
    }

    @PostMapping("/addItem/{listName}")
    public RedirectView addItem(@PathVariable String listName, Principal principal, @ModelAttribute ShopItem item) {
        Household household = getHousehold(principal);
        Map<String, List<ShopItem>> shopLists = household.getLists();
        shopLists.get(listName).add(item);
        repositoryH.save(household);
        return new RedirectView("/shop/edit/" + listName);
    }

    @PostMapping("/delete/{name}/{index}")
    public RedirectView addItem(@PathVariable String name, Principal principal, @PathVariable int index) {
        Household household = getHousehold(principal);
        Map<String, List<ShopItem>> shopLists = household.getLists();
        shopLists.get(name).remove(index);
        repositoryH.save(household);
        return new RedirectView("/shop/edit/" + name);
    }

}
