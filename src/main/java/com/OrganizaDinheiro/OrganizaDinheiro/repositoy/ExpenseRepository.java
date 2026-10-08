package com.OrganizaDinheiro.OrganizaDinheiro.repositoy;

import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userid);

    List<Expense>findByUserIdAndCategory(Long userId,Category category);

    List<Expense> findByUserIdAndDate(Long userId,LocalDate date);

    @Query("""
  SELECT COALESCE(SUM(e.value), 0)
  FROM Expense e 
  WHERE e.user.id = :userId
   AND e.date >= :startDate
   AND e.date < :endDate
  GROUP BY category
""")
    BigDecimal sumValueByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
