package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import jakarta.validation.constraints.*;
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
    @Size(max=100)
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal value;
    @NotNull
    private Category category;
    @NotNull
    private LocalDate date;
}
