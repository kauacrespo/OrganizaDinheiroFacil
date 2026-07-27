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


    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    void verifyUser(Long userID){
        userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    void verifyExpense(Long expenseID,Long userID){
        expenseRepository.findById(expenseID)
                .filter(expense -> expense.getUser().getId().equals(userID))
                .orElseThrow(() -> new RuntimeException("despesa nao existe para esse usuario"));

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
        verifyExpense(id, userId);
        return expenseRepository.findByIdUser(id);

    }

    public Optional<Expense> updateExpenseById(Long id, ExpenseRequest request, Long userId) {
        verifyUser(userId);
        verifyExpense(id,userId);

        Expense expenseUpdate = expenseRepository.findById(id).get();

        expenseUpdate.setDescription(request.getDescription());
        expenseUpdate.setCategory(request.getCategory());
        expenseUpdate.setName(request.getName());
        expenseUpdate.setValue(request.getValue());
        expenseUpdate.setDate(request.getDate());

        expenseRepository.save(expenseUpdate);
        return Optional.of(expenseUpdate);
    }

    public void deleteExpenseById(Long id , Long userId) {
        verifyUser(userId);
        verifyExpense(id,userId);
        expenseRepository.deleteById(id);
    }

    public Expense createExpense(Long userId, ExpenseRequest request) {

        Expense expense = new Expense();
            expense.setCategory(request.getCategory());
            expense.setDate(request.getDate());
            expense.setValue(request.getValue());
            expense.setName(request.getName());
            expense.setUser(userRepository.findById(userId).get());
            expenseRepository.save(expense);

            return expense;
    }
}
