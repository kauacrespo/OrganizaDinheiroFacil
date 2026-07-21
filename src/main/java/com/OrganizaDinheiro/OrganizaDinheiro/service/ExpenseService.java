package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.ExpenseRepository;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    final private ExpenseRepository expenseRepository;

    final private UserRepository userRepository;


    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, Expense expense) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;

    }


    void verifyUser(Long userID){
        userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    void verifyExpense(Long expenseID,Long userID){
        expenseRepository.findById(expenseID)
                .orElseThrow(() -> new RuntimeException("Expense nao encontrado"));
        if(expenseRepository.equals(userID)){

        };
    }

    public List<Expense> getExpenses(Long userId) {
        verifyUser(userId);
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getExpensesByCategory(Long userId, Category category) {
        verifyUser(userId);
        return expenseRepository.findByUserAndCategoryAndUserId(userId,category);
    }

    public List<Expense> getExpensesByDate(Long userId, LocalDate date) {
        verifyUser(userId);
        return expenseRepository.findByUserAndDateAndUserId(userId,date );
    }

    public Optional<Expense> getExpenseById(Long userId, Long id) {
        verifyUser(userId);
        verifyExpense(userId);
        return expenseRepository.findByIdUser(id);

    }

    public Optional<Expense> updateExpenseById(Long id, ExpenseRequest expense, Long userId) {
        verifyUser(userId);
        verifyExpense(userId);
        expense.setCategory(expense.getCategory());
        expense.setDate(expense.getDate());
        expense.setValue(expense.getValue());
        expense.setName(expense.getName());
        expenseRepository.save(expense);
        return expenseRepository.findByIdUser(id);
    }

    public void deleteExpenseById(Long id , Long userId) {
        verifyUser(userId);
        expenseRepository.deleteById(id);
    }
}
