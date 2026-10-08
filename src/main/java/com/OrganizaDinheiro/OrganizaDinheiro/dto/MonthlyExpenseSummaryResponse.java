package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MonthlyExpenseSummaryResponse {

    private int year;
    private int month;
    private BigDecimal total;
}
