package com.OrganizaDinheiro.OrganizaDinheiro.repositoy;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userid);

    List<Expense>findByUserIdAndCategory(Long userId,Category category);

    List<Expense> findByUserIdAndDate(Long userId,LocalDate date);

}
