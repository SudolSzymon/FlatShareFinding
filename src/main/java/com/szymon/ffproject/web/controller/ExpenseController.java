package com.szymon.ffproject.web.controller;

import com.szymon.ffproject.database.entity.Expense;
import com.szymon.ffproject.database.entity.Household;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.HouseholdRepository;
import com.szymon.ffproject.database.repository.UserRepository;
import com.szymon.ffproject.web.util.FormUtil;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

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
    public String add(Model model, Expense expense) {
        FormUtil.addForm(model, expense, "/expense/create", "Create Expense");
        return "add";
    }

    @PostMapping(value = "/create")
    public RedirectView create(Principal principal, @ModelAttribute Expense expense) {
        User mainUser = repositoryU.findById(principal.getName()).get();
        Household household = repositoryH.findById(mainUser.getHouseName()).get();
        Set<String> members = household.getMembers();
        if (members.contains(expense.getBorrower()) && members.contains(expense.getLender())) {
            household.addExpense(expense);
            repositoryH.save(household);
        } else throw badRequestException("Invalid borrower or lender");
        return new RedirectView("/house/" + household.getName());
    }

}
