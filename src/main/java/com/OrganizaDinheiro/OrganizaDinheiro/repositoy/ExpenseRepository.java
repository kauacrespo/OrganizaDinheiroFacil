package com.OrganizaDinheiro.OrganizaDinheiro.repositoy;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userid);

    List<Expense>findByUserAndCategoryAndUserId(Long userId,Category category);

    List<Expense> findByUserAndDateAndUserId(Long userId,LocalDate date);

    Optional<Expense> findByUserAndNameAndUserId(Long userId,String name);

    Optional<Expense> findByIdUser(Long id);


}
