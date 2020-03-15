package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.ShopItem;
import com.szymon.ffproject.database.entity.ShopList;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.FieldUtil;
import java.security.Principal;
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
        FieldUtil.addList(model, household.getLists().values(), "Shopping Lists", "../delete/id", "../edit/id");
        FieldUtil.addObject(model, getAttribute(model, "object", ShopList.class), "../create", "New List");
        return "generic/genericListFormOnSide";
    }

    @PostMapping("/create")
    public RedirectView create(Principal principal, @ModelAttribute("object") ShopList list) {
        Household household = getHousehold(principal);
        household.addList(list);
        repositoryH.save(household);
        return new RedirectView("/shop/edit/" + list.getName());
    }

    @RequestMapping("/edit/{name}")
    public String edit(Model model, @PathVariable String name, Principal principal) {
        Household household = getHousehold(principal);
        FieldUtil.addList(model, household.getList(name).getItemList(), name, "../delete/item/" + name, null);
        FieldUtil.addObject(model, new ShopItem(), "/shop/add/item/" + name, "New Item");
        return "generic/genericListFormOnSide";
    }

    @RequestMapping("/edit/id/{index}")
    public RedirectView edit(@PathVariable Integer index, Principal principal) {
        Household household = getHousehold(principal);
        String name = household.getListByIndex(index).getName();
        return new RedirectView("/shop/edit/" + name);
    }

    @RequestMapping("/delete/{name}")
    public RedirectView delete(@PathVariable String name, Principal principal) {
        Household household = getHousehold(principal);
        household.getLists().remove(name);
        repositoryH.save(household);
        return new RedirectView("/shop/view/all");
    }

    @RequestMapping("/delete/id/{index}")
    public RedirectView delete(@PathVariable Integer index, Principal principal) {
        Household household = getHousehold(principal);
        String name = household.getListByIndex(index).getName();
        return new RedirectView("/shop/delete/" + name);
    }


    @PostMapping("/add/item/{listName}")
    public RedirectView addItem(@PathVariable String listName, Principal principal, @ModelAttribute ShopItem item) {
        Household household = getHousehold(principal);
        household.getList(listName).getItemList().add(item);
        repositoryH.save(household);
        return new RedirectView("/shop/edit/" + listName);
    }

    @RequestMapping("/delete/item/{name}/{index}")
    public RedirectView addItem(@PathVariable String name, Principal principal, @PathVariable int index) {
        Household household = getHousehold(principal);
        household.getList(name).getItemList().remove(index);
        repositoryH.save(household);
        return new RedirectView("/shop/edit/" + name);
    }

}
