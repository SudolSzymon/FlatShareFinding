package com.szymon.ffproject.controller;

import com.szymon.ffproject.database.entity.Expense;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.service.HouseholdService;
import com.szymon.ffproject.service.UserService;
import com.szymon.ffproject.util.FieldUtil;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/expense")
public class ExpenseController extends GenericController {


    public ExpenseController(HouseholdService householdService,
                             UserService userService) {
        super(householdService, userService);
    }

    @GetMapping("/view")
    public String view(Model model, Principal principal) {
        Expense expense = getAttribute(model, "object", Expense.class);
        Household house = serviceU.getHousehold(principal);
        FieldUtil.addList(model, house.getExpenses().values(), "Expenses", "./delete", null);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("lender", house.getMembers());
        values.put("borrower", house.getMembers());
        FieldUtil.addObject(model, expense, "/expense/create", "New Expense", values);
        return "generic/genericListFormOnSide";
    }


    @PostMapping(value = "/create")
    public String create(Principal principal, @Valid @ModelAttribute("expense") Expense expense,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            storeBindingResult(expense, bindingResult, redirectAttributes);
            return "redirect:/expense/view";
        }

        if (!serviceH.addExpense(serviceU.getHousehold(principal), expense))
            throw badRequestException("Invalid borrower or lender");
        return "redirect:/expense/view";
    }


    @RequestMapping(value = "/delete/{id}")
    public String delete(Principal principal, @PathVariable String id) {
        if (!serviceH.deleteExpense(serviceU.getHousehold(principal), id, serviceU.getUser(principal)))
            throw unAuthorisedException("user not  authorised to delete this  expense");

        return "redirect:/expense/view";
    }

}
