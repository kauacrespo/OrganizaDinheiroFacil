package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseRequest {
    private String name;
    private String description;
    private BigDecimal value;
    private Category category;
    private LocalDate date;
}
