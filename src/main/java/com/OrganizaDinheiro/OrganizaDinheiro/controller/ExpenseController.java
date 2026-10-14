package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseResponse;
import com.OrganizaDinheiro.OrganizaDinheiro.mapper.ExpenseMapper;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.service.CurrentUserService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.MonthlyExpenseSummaryResponse;

import java.math.BigDecimal;
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

    private List<ExpenseResponse> toResponseList(List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @PostMapping()
    public ResponseEntity<ExpenseResponse> createExpense(@Validated @RequestBody ExpenseRequest request) {
       Expense expense =  expenseService.createExpense(currentUserService.getCurrentUserId(), request);
       ExpenseResponse response = ExpenseMapper.toResponse(expense);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ExpenseResponse>> getExpenses() {
        List<Expense> expenses = expenseService.getExpenses(currentUserService.getCurrentUserId());
        return ResponseEntity.ok(toResponseList(expenses));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(@PathVariable("category") Category category) {
        List<Expense> expenses = expenseService.getExpensesByCategory(currentUserService.getCurrentUserId(), category);
        return ResponseEntity.status(HttpStatus.OK).body(toResponseList(expenses));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDate(@PathVariable("date") LocalDate date) {
        List<Expense> expenses = expenseService.getExpensesByDate(currentUserService.getCurrentUserId(), date);
        return ResponseEntity.status(HttpStatus.OK).body(toResponseList(expenses));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpenseById(@PathVariable("id") Long expenseId, @Validated @RequestBody ExpenseRequest request){
        expenseService.updateExpenseById(
                expenseId,
                currentUserService.getCurrentUserId(),
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable("id") Long expenseId) {
        expenseService.deleteExpenseById(expenseId,currentUserService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("summary/month")
    public ResponseEntity<MonthlyExpenseSummaryResponse> getMonthlySummary(@RequestParam int year, @RequestParam int month){

        Long userid = currentUserService.getCurrentUserId();

        BigDecimal total = expenseService.getMonthlyTotal(userid,year,month);

        MonthlyExpenseSummaryResponse response =
                new MonthlyExpenseSummaryResponse(
                        year,
                        month,
                        total
                );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}