package com.szymon.ffproject.controller;

import com.szymon.ffproject.dao.S3DAO;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.ShopItem;
import com.szymon.ffproject.database.entity.ShopList;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.FieldUtil;
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


    public ShoppingListController(HouseholdService householdService,
                                  UserService userService, S3DAO s3DAO) {
        super(householdService, userService, s3DAO);
    }

    @ModelAttribute("shopLists")
    public Map<String, List<ShopItem>> setUpList() {
        return new HashMap<>();
    }

    @GetMapping("/view/all")
    public String view(Model model, Principal principal) {
        Household household = serviceU.getHousehold(principal);
        FieldUtil.addList(model, household.getLists().values(), "Shopping Lists", "../delete/id", "../edit/id");
        FieldUtil.addObject(model, getAttribute(model, "object", ShopList.class), "../create", "New List");
        return "generic/genericListFormOnSide";
    }

    @PostMapping("/create")
    public RedirectView create(Principal principal, @ModelAttribute("object") ShopList list) {
        serviceH.addShopList(serviceU.getHousehold(principal), list);
        return new RedirectView("/shop/edit/" + list.getName());
    }

    @RequestMapping("/edit/{name}")
    public String edit(Model model, @PathVariable String name, Principal principal) {
        Household household = serviceU.getHousehold(principal);
        FieldUtil.addList(model, household.getList(name).getItemList(), name, "../delete/item/" + name, null);
        FieldUtil.addObject(model, new ShopItem(), "/shop/add/item/" + name, "New Item");
        return "generic/genericListFormOnSide";
    }

    @RequestMapping("/edit/id/{index}")
    public RedirectView edit(@PathVariable Integer index, Principal principal) {
        Household household = serviceU.getHousehold(principal);
        String name = serviceH.getListByIndex(household, index).getName();
        return new RedirectView("/shop/edit/" + name);
    }

    @RequestMapping("/delete/id/{index}")
    public RedirectView delete(@PathVariable Integer index, Principal principal) {
        serviceH.removeShopList(serviceU.getHousehold(principal), index);
        return new RedirectView("/shop/view/all");
    }


    @PostMapping("/add/item/{listName}")
    public RedirectView addItem(@PathVariable String listName, Principal principal, @ModelAttribute ShopItem item) {
        serviceH.addShopListItem(serviceU.getHousehold(principal), listName, item);
        return new RedirectView("/shop/edit/" + listName);
    }

    @RequestMapping("/delete/item/{listName}/{index}")
    public RedirectView addItem(@PathVariable String listName, Principal principal, @PathVariable int index) {
        serviceH.removeShopListItem(serviceU.getHousehold(principal), listName, index);
        return new RedirectView("/shop/edit/" + listName);
    }

}
