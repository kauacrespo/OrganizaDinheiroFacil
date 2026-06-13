package com.OrganizaDinheiro.OrganizaDinheiro.repositoy;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);

    List<Expense>findByUserAndCategory(User user,Category category);

    List<Expense> findByUserAndDate(User user,LocalDate date);

    Optional<Expense> findByUserAndName(User user,String name);
}
