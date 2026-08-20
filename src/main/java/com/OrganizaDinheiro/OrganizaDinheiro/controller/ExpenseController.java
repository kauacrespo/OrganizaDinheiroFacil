package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.service.CurrentUserService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {


    private final ExpenseService expenseService;
    private final CurrentUserService currentUserService;

    public ExpenseController(ExpenseService expenseService,CurrentUserService currentUserService) {
        this.expenseService = expenseService;
        this.currentUserService = currentUserService;
    }

    @PostMapping()
    public ResponseEntity<ExpenseRequest> createExpense(@Validated @RequestBody ExpenseRequest request) {
        expenseService.createExpense(currentUserService.getCurrentUserId(), request);
       return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping()
    public ResponseEntity<List<Expense>> getExpenses() {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpenses(currentUserService.getCurrentUserId()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable("category") Category category) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByCategory(currentUserService.getCurrentUserId(), category));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Expense>> getExpensesByDate(@PathVariable("date") LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByDate(currentUserService.getCurrentUserId(),date));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpenseById(@PathVariable("id") Long expenseId,@Validated @RequestBody ExpenseRequest request) {
        expenseService.updateExpenseById(expenseId,expenseId,request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> deleteExpenseById(@PathVariable("id") Long expenseId) {
        expenseService.deleteExpenseById(currentUserService.getCurrentUserId(),expenseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}