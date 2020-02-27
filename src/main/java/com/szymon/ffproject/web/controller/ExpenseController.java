package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.Expense;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.FormUtil;
import java.security.Principal;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        Household house = getHousehold(principal);
        model.addAttribute("list", house.getExpenses());
        return "expenseList";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Expense expense = getAttribute(model, "object", Expense.class);
        FormUtil.addForm(model, expense, "/expense/create", "Create Expense");
        return "add";
    }

    @PostMapping(value = "/create")
    public String create(Principal principal, @Valid @ModelAttribute("object") Expense object,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            storeBindingResult(object, bindingResult, redirectAttributes);
            return "redirect:/expense/add";
        }
        Household household = getHousehold(principal);
        Set<String> members = household.getMembers();
        if (members.contains(object.getBorrower()) && members.contains(object.getLender())) {
            household.addExpense(object);
            repositoryH.save(household);
        } else throw badRequestException("Invalid borrower or lender");
        return "redirect:/house/view";
    }

}
