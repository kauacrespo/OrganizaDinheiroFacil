package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {


    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long idUser = Long.parseLong(String.valueOf(principal));

        return idUser;
    }

    @PostMapping()
    public ResponseEntity<ExpenseRequest> createExpense(@RequestBody ExpenseRequest request) {
        expenseService.createExpense(request, getCurrentUserId());
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<Expense>> getExpenses() {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpenses(getCurrentUserId()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable("category") Category category) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByCategory(getCurrentUserId(), category));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Expense>> getExpensesByDate(@PathVariable("date") LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByDate(getCurrentUserId(),date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional> getExpenseById(@PathVariable("id") Long expenseId) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpenseById(getCurrentUserId(),expenseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Expense>> updateExpenseById(@PathVariable("id") Long expenseId,@RequestBody ExpenseRequest request) {
        expenseService.updateExpenseById(expenseId,request,getCurrentUserId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Expense> deleteExpenseById(Long expenseId) {
        expenseService.deleteExpenseById(getCurrentUserId(),expenseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}