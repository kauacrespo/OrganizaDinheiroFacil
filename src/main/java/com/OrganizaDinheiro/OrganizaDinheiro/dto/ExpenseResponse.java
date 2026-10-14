package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ExpenseResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal value;
    private Category category;
    private LocalDate date;
}
