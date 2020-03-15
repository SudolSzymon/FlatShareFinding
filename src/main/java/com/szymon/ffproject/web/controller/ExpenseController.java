package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.Expense;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.FieldUtil;
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

    public ExpenseController(UserRepository repository,
                             HouseholdRepository repositoryH) {
        super(repository, repositoryH);
    }

    @GetMapping("/view")
    public String view(Model model, Principal principal) {
        Expense expense = getAttribute(model, "object", Expense.class);
        Household house = getHousehold(principal);
        FieldUtil.addList(model, house.getExpenses(), "Expenses", "./delete", null);
        Map<String, Set<String>> values = new HashMap<>();
        values.put("lender", house.getMembers());
        values.put("borrower", house.getMembers());
        FieldUtil.addObject(model, expense, "/expense/create", "New Expense", values);
        return "generic/genericListFormOnSide";
    }


    @PostMapping(value = "/create")
    public String create(Principal principal, @Valid @ModelAttribute("object") Expense object,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            storeBindingResult(object, bindingResult, redirectAttributes);
            return "redirect:/expense/view";
        }
        Household household = getHousehold(principal);
        Set<String> members = household.getMembers();
        if (members.contains(object.getBorrower()) && members.contains(object.getLender())) {
            household.addExpense(object);
            repositoryH.save(household);
        } else throw badRequestException("Invalid borrower or lender");
        return "redirect:/expense/view";
    }


    @RequestMapping(value = "/delete/{index}")
    public String delete(Principal principal, @PathVariable Integer index) {
        Household household = getHousehold(principal);
        Expense expense = household.getExpenses().get(index);
        if (expense.getLender().equals(principal.getName()))
            household.getExpenses().remove(expense);
        else
            throw unAuthorisedException("user not  authorised to delete this  expense");
        repositoryH.save(household);

        return "redirect:/expense/view";
    }

}
