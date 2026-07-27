package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Max(100)
    private String description;
    @NotNull
    @Min(1)
    private BigDecimal value;
    @NotNull
    private Category category;
    @NotNull
    private LocalDate date;
}
