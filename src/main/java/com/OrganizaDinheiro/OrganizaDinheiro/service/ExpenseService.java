package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.ExpenseRepository;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import java.math.BigDecimal;

public class ExpenseService {

    final private ExpenseRepository expenseRepository;

    final private UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public Expense createExpense(ExpenseRequest request,Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        if(request.getValue().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("O valor deve ser maior que zero");

        }

        Expense  expense = new Expense();
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setValue(request.getValue());
        expense.setName(request.getName());
        expense.setDescription(request.getDescription());

        expense.setUser(user);

        return expenseRepository.save(expense);
    }
}
