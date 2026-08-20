package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.ExpenseRepository;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));
    }

    private Expense findExpenseForUser(Long expenseId, Long userId) {
        return expenseRepository.findById(expenseId)
                .filter(expense ->
                        expense.getUser().getId().equals(userId))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Despesa não encontrada para este usuário"
                        ));
    }

    public List<Expense> getExpenses(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getExpensesByCategory(Long userId, Category category) {
        return expenseRepository.findByUserIdAndCategory(userId,category);
    }

    public List<Expense> getExpensesByDate(Long userId, LocalDate date) {
        return expenseRepository.findByUserIdAndDate(userId,date );
    }


    public Expense updateExpenseById(Long expenseId, Long userId, ExpenseRequest expenseRequest) {

        Expense expense = findExpenseForUser(expenseId,userId);

        expense.setName(expenseRequest.getName());
        expense.setDescription(expenseRequest.getDescription());
        expense.setCategory(expenseRequest.getCategory());
        expense.setDate(expenseRequest.getDate());
        expense.setValue(expenseRequest.getValue());

        return expenseRepository.save(expense);
    }

    public void deleteExpenseById(Long id , Long userId) {

        Expense expense = findExpenseForUser(id,userId);
        expenseRepository.delete(expense);
    }

    public Expense createExpense(Long userId, ExpenseRequest request) {

        User user = findUser(userId);

        Expense expense = new Expense();

            expense.setUser(user);
            expense.setCategory(request.getCategory());
            expense.setDate(request.getDate());
            expense.setValue(request.getValue());
            expense.setDescription(request.getDescription());
            expense.setName(request.getName());

            return expenseRepository.save(expense);
    }
}
