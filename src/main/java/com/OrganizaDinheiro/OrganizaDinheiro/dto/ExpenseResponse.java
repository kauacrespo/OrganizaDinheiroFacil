package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseResponse {

    private String name;
    private String description;
    private BigDecimal value;
    private Category category;
    private LocalDate date;
}
