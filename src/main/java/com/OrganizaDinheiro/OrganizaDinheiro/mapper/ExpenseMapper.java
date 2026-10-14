package com.OrganizaDinheiro.OrganizaDinheiro.mapper;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseResponse;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;

public class ExpenseMapper {

    public static  ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getValue(),
                expense.getCategory(),
                expense.getDate());
    }
}
