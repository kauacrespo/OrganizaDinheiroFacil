package com.OrganizaDinheiro.OrganizaDinheiro;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.exception.ExpenseNotFoundException;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.ExpenseRepository;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import com.OrganizaDinheiro.OrganizaDinheiro.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void shouldCreateExpenseSuccessfully() {
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        ExpenseRequest request = new ExpenseRequest();
        request.setName("Almoço");
        request.setDescription("Almoço de trabalho");
        request.setValue(new BigDecimal("35.50"));
        request.setCategory(Category.ALIMENTAÇAO);
        request.setDate(LocalDate.of(2026, 10, 14));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> {
                    Expense expense = invocation.getArgument(0);
                    expense.setId(100L);
                    return expense;
                });

        // Act: executar o método que queremos testar
        Expense result = expenseService.createExpense(userId, request);

        // Assert: verificar o resultado
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Almoço", result.getName());
        assertEquals("Almoço de trabalho", result.getDescription());
        assertEquals(
                new BigDecimal("35.50"),
                result.getValue()
        );
        assertEquals(
                LocalDate.of(2026, 10, 14),
                result.getDate()
        );
        assertNotNull(result.getUser());
        assertEquals(userId, result.getUser().getId());

        verify(userRepository).findById(userId);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExists(){
        Long userid = 10L;
        when(userRepository.findById(userid))
                .thenReturn(Optional.empty());
// Act
        ExpenseNotFoundException exception = assertThrows(
                ExpenseNotFoundException.class,() -> expenseService.createExpense(userid, null)

        );
// Assert
        assertEquals(
                "Usuario nao encontrado",
                exception.getMessage()
        );

        verify(userRepository).findById(userid);
        verifyNoInteractions(expenseRepository);
    }

    @Test
    void shouldUpdateExpenseSucessfully() {
        Long userid = 10L;
        Long expenseid = 50L;

        User user = new User();
        user.setId(userid);

        Expense expense = new Expense();
        expense.setId(expenseid);
        expense.setUser(user);

        ExpenseRequest request = new ExpenseRequest();
        request.setName("Mercado");
        request.setDescription("Compras do Mes");
        request.setValue(new BigDecimal("250.90"));
        request.setCategory(Category.ALIMENTAÇAO);
        request.setDate(LocalDate.of(2026, 11, 6));

        when(expenseRepository.findById(expenseid))
                .thenReturn(Optional.of(expense));

        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
// Act
        Expense result = expenseService.updateExpenseById(
                expenseid,
                userid,
                request
        );
// Assert
        assertNotNull(result);
        assertEquals("Mercado", result.getName());
        assertEquals("Compras do Mes", result.getDescription());
        assertEquals(
                new BigDecimal("250.90"),
                result.getValue());
        assertEquals(Category.ALIMENTAÇAO, result.getCategory());
        assertEquals(LocalDate.of(2026, 11, 6), result.getDate());

        assertNotNull(result.getUser());
        assertEquals(userid, result.getUser().getId());

        verify(expenseRepository).findById(expenseid);
        verify(expenseRepository).save(expense);
    }

    @Test
    void shouldThrowExceptionWhenExpenseBelongsToAnotherUser(){
        Long userid = 10L;
        Long expenseid = 50L;
        Long ownerId = 20L;

        User user = new User();
        user.setId(userid);

        User owner = new User();
        owner.setId(ownerId);

        Expense expense = new Expense();
        expense.setId(ownerId);
        expense.setUser(owner);

        ExpenseRequest request = new ExpenseRequest();
        request.setName("Gasolina");
        request.setDescription("Combustivel do Carro");
        request.setValue(new BigDecimal("100.0"));
        request.setCategory(Category.TRANSPORTE);
        request.setDate(LocalDate.of(2026, 11, 5));

        when(expenseRepository.findById(expenseid))
                .thenReturn(Optional.of(expense));
// Act
        ExpenseNotFoundException exception = assertThrows(
                ExpenseNotFoundException.class,() -> expenseService.updateExpenseById(
                        expenseid,
                        userid,
                        request
                )
        );
// Assert
        assertEquals(
                "Despesa nao encontrada para este usuario",
                exception.getMessage()
        );

        verify(expenseRepository).findById(expenseid);
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void shouldDeleteExpenseSuccessfully(){
        Long userId = 10L;
        Long expenseId = 50L;

        User user = new User();
        user.setId(userId);

        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setUser(user);

        when(expenseRepository.findById(expenseId))
                .thenReturn(Optional.of(expense));

        expenseService.deleteExpenseById(
                expenseId,
                userId
        );

        verify(expenseRepository).findById(expenseId);
        verify(expenseRepository).delete(expense);
    }

    @Test
    void shouldThrowExceptionWhenDeletingExpenseBelongsToAnotherUser() {
        Long userId = 10L;
        Long ownerId = 20L;
        Long expenseId = 50L;

        User user = new User();
        user.setId(userId);

        User owner = new User();
        owner.setId(ownerId);

        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setUser(owner);

        when(expenseRepository.findById(expenseId))
                .thenReturn(Optional.of(expense));


        ExpenseNotFoundException exception = assertThrows(
                ExpenseNotFoundException.class,() -> expenseService.deleteExpenseById(
                        expenseId,
                        userId
                )
        );

        assertEquals(
                "Despesa nao encontrada para este usuario",
                exception.getMessage()
        );

        verify(expenseRepository).findById(expenseId);
        verify(expenseRepository,never()).delete(any(Expense.class));
    }
}
