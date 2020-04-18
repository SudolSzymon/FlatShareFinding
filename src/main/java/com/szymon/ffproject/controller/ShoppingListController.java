package com.szymon.ffproject.controller;

import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.ShopItem;
import com.szymon.ffproject.database.entity.ShopList;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.FieldUtil;
import java.security.Principal;
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
                                  UserService userService) {
        super(householdService, userService);
    }

    @GetMapping("/view/all")
    public String view(Model model, Principal principal) {
        Household household = serviceU.getHousehold(principal);
        FieldUtil.addList(model, household.getLists().values(), "Shopping Lists", "../delete", "../edit");
        FieldUtil.addObject(model, getAttribute(model, "object", ShopList.class), "../create", "New List");
        return "generic/genericListFormOnSide";
    }

    @PostMapping("/create")
    public RedirectView create(Principal principal, @ModelAttribute("object") ShopList list) {
        serviceH.addShopList(serviceU.getHousehold(principal), list);
        return new RedirectView("/shop/edit/" + list.getEntityID());
    }

    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable String id, Principal principal) {
        Household household = serviceU.getHousehold(principal);
        ShopList list = serviceH.getList(household, id);
        FieldUtil.addList(model, list.getItemList(), list.getName(), "../delete/item/" + id, null);
        FieldUtil.addObject(model, new ShopItem(), "/shop/add/item/" + id, "New Item");
        return "generic/genericListFormOnSide";
    }

    @RequestMapping("/delete/{id}")
    public RedirectView delete(@PathVariable String id, Principal principal) {
        serviceH.removeShopList(serviceU.getHousehold(principal), id);
        return new RedirectView("/shop/view/all");
    }


    @PostMapping("/add/item/{id}")
    public RedirectView addItem(@PathVariable String id, Principal principal, @ModelAttribute ShopItem item) {
        serviceH.addShopListItem(serviceU.getHousehold(principal), id, item);
        return new RedirectView("/shop/edit/" + id);
    }

    @RequestMapping("/delete/item/{listID}/{itemID}")
    public RedirectView addItem(@PathVariable String listID, Principal principal, @PathVariable String itemID) {
        serviceH.removeShopListItem(serviceU.getHousehold(principal), listID, itemID);
        return new RedirectView("/shop/edit/" + listID);
    }

}
