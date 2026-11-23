package com.OrganizaDinheiro.OrganizaDinheiro;

import com.OrganizaDinheiro.OrganizaDinheiro.controller.ExpenseController;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.ExpenseRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.exception.ExpenseNotFoundException;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Expense;
import com.OrganizaDinheiro.OrganizaDinheiro.service.CurrentUserService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.ExpenseService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.JwtService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ExpenseController.class)

public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @Test
    @WithMockUser
    void shouldCreateExpenseSuccessfully() throws Exception {

        // Arrange
        Long userId = 10L;

        ExpenseRequest request = new ExpenseRequest();
        request.setName("Almoço");
        request.setDescription("Almoço de trabalho");
        request.setValue(new BigDecimal("35.50"));
        request.setCategory(Category.ALIMENTAÇAO);
        request.setDate(LocalDate.of(2026, 10, 14));

        Expense expense = new Expense();
        expense.setId(100L);
        expense.setName(request.getName());
        expense.setDescription(request.getDescription());
        expense.setValue(request.getValue());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        when(expenseService.createExpense(eq(userId), any(ExpenseRequest.class)))
                .thenReturn(expense);

        // Act + Assert
        mockMvc.perform(
                        post("/expenses")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Almoço",
                                            "description": "Almoço de trabalho",
                                            "value": 35.50,
                                            "category": "ALIMENTAÇAO",
                                            "date": "2026-10-14"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("Almoço"))
                .andExpect(jsonPath("$.description").value("Almoço de trabalho"))
                .andExpect(jsonPath("$.value").value(35.50))
                .andExpect(jsonPath("$.date").value("2026-10-14"));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenNameIsBlanck() throws Exception {

        mockMvc.perform(
                post("/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "",
                                "description": "Almoço de trabalho",
                                "value": 35.50,
                                "category": "ALIMENTAÇAO",
                                "date": "2026-10-14"
                            }
                            """)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.name").exists());

        verifyNoInteractions(expenseService);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenValueIsZero() throws Exception {

        mockMvc.perform(
                        post("/expenses")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        { 
                                        
                                        "name": "Almoço",
                                        "description": "Almoço de trabalho",
                                        "value": 0,
                                        "category": "ALIMENTAÇAO",
                                        "date": "2026-10-14"
                                        
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.value").exists());

        verifyNoInteractions(expenseService);
    }

    @Test
    @WithMockUser
    void shouldReturnExpensesSuccessfully() throws Exception {

        Long userId = 10L;

        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setName("Almoço");
        expense1.setDescription("Almoço de trabalho");
        expense1.setValue(new BigDecimal("35.50"));
        expense1.setCategory(Category.ALIMENTAÇAO);
        expense1.setDate(LocalDate.of(2026, 10, 14));

        Expense expense2 = new Expense();
        expense2.setId(2L);
        expense2.setName("Uber");
        expense2.setDescription("Transporte");
        expense2.setValue(new BigDecimal("20.00"));
        expense2.setCategory(Category.TRANSPORTE);
        expense2.setDate(LocalDate.of(2026, 10, 15));

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        when(expenseService.getExpenses(userId))
                .thenReturn(List.of(expense1, expense2));

        mockMvc.perform(
                        get("/expenses")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Almoço"))
                .andExpect(jsonPath("$[0].value").value(35.50))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Uber"))
                .andExpect(jsonPath("$[1].value").value(20.00));

        verify(expenseService).getExpenses(userId);
    }

    @Test
    @WithMockUser
    void shouldReturnExpensesByCategorySuccessfully() throws Exception {

        Long userId = 10L;

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setName("Almoço");
        expense.setDescription("Almoço de trabalho");
        expense.setValue(new BigDecimal("35.50"));
        expense.setCategory(Category.ALIMENTAÇAO);
        expense.setDate(LocalDate.of(2026, 10, 14));

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        when(expenseService.getExpensesByCategory(
                userId,
                Category.ALIMENTAÇAO
        )).thenReturn(List.of(expense));

        mockMvc.perform(
                        get("/expenses/category/{category}", "ALIMENTAÇAO")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Almoço"))
                .andExpect(jsonPath("$[0].category").value("ALIMENTAÇAO"));

        verify(expenseService)
                .getExpensesByCategory(userId, Category.ALIMENTAÇAO);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCategoryIsInvalid() throws Exception {

        mockMvc.perform(
                        get("/expenses/category/{category}", "INVALIDA")
                                .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Categoria inválida. Informe uma categoria válida."))
                .andExpect(jsonPath("$.errors").isEmpty());

        verifyNoInteractions(expenseService);
    }

    @Test
    @WithMockUser
    void shouldReturnExpensesByDateSuccessfully() throws Exception {

        Long userId = 10L;

        LocalDate date = LocalDate.of(2026, 10, 14);

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setName("Almoço");
        expense.setDescription("Almoço de trabalho");
        expense.setValue(new BigDecimal("35.50"));
        expense.setCategory(Category.ALIMENTAÇAO);
        expense.setDate(date);

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        when(expenseService.getExpensesByDate(userId, date))
                .thenReturn(List.of(expense));

        mockMvc.perform(
                        get("/expenses/date/{date}", "2026-10-14")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Almoço"))
                .andExpect(jsonPath("$[0].value").value(35.50))
                .andExpect(jsonPath("$[0].date").value("2026-10-14"));

        verify(expenseService)
                .getExpensesByDate(userId, date);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenDateIsInvalid() throws Exception {

        mockMvc.perform(
                        get("/expenses/date/{date}", "banana")
                                .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Parâmetro inválido na requisição."))
                .andExpect(jsonPath("$.errors").isEmpty());

        verifyNoInteractions(expenseService);
    }

    @Test
    @WithMockUser
    void shouldUpdateExpenseSuccessfully() throws Exception {

        Long userId = 10L;
        Long expenseId = 1L;

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        mockMvc.perform(
                        put("/expenses/{id}", expenseId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "name": "Almoço atualizado",
                                "description": "Almoço de trabalho atualizado",
                                "value": 40.00,
                                "category": "ALIMENTAÇAO",
                                "date": "2026-10-14"
                            }
                            """)
                )
                .andExpect(status().isNoContent());

        verify(expenseService).updateExpenseById(
                eq(expenseId),
                eq(userId),
                any(ExpenseRequest.class)
        );
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUpdatingWithBlankName() throws Exception {

        mockMvc.perform(
                        put("/expenses/{id}", 1L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "name": "",
                                "description": "Almoço",
                                "value": 40.00,
                                "category": "ALIMENTAÇAO",
                                "date": "2026-10-14"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.name").exists());

        verifyNoInteractions(expenseService);
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenUpdatingExpenseFromAnotherUser() throws Exception {

        Long userId = 10L;
        Long expenseId = 1L;

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        doThrow(new ExpenseNotFoundException(
                404,
                "Despesa nao encontrada para este usuario"
        ))
                .when(expenseService)
                .updateExpenseById(
                        eq(expenseId),
                        eq(userId),
                        any(ExpenseRequest.class)
                );

        mockMvc.perform(
                        put("/expenses/{id}", expenseId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "name": "Almoço atualizado",
                                "description": "Almoço atualizado",
                                "value": 40.00,
                                "category": "ALIMENTAÇAO",
                                "date": "2026-10-14"
                            }
                            """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Despesa nao encontrada para este usuario"));

        verify(expenseService).updateExpenseById(
                eq(expenseId),
                eq(userId),
                any(ExpenseRequest.class)
        );
    }

    @Test
    @WithMockUser
    void shouldDeleteExpenseSuccessfully() throws Exception {

        Long userId = 10L;
        Long expenseId = 1L;

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        mockMvc.perform(
                        delete("/expenses/{id}", expenseId)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(expenseService).deleteExpenseById(
                eq(expenseId),
                eq(userId)
        );
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenDeletingExpenseFromAnotherUser() throws Exception {

        Long userId = 10L;
        Long expenseId = 1L;

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        doThrow(new ExpenseNotFoundException(
                404,
                "Despesa nao encontrada para este usuario"
        ))
                .when(expenseService)
                .deleteExpenseById(
                        eq(expenseId),
                        eq(userId)
                );

        mockMvc.perform(
                        delete("/expenses/{id}", expenseId)
                                .with(csrf())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Despesa nao encontrada para este usuario"));

        verify(expenseService).deleteExpenseById(
                eq(expenseId),
                eq(userId)
        );
    }

    @Test
    @WithMockUser
    void shouldReturnMonthlySummarySuccessfully() throws Exception {

        Long userId = 10L;
        int year = 2026;
        int month = 10;

        BigDecimal total = new BigDecimal("155.50");

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        when(expenseService.getMonthlyTotal(userId, year, month))
                .thenReturn(total);

        mockMvc.perform(
                        get("/expenses/summary/month")
                                .param("year", String.valueOf(year))
                                .param("month", String.valueOf(month))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2026))
                .andExpect(jsonPath("$.month").value(10))
                .andExpect(jsonPath("$.total").value(155.50));

        verify(expenseService).getMonthlyTotal(
                userId,
                year,
                month
        );
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenYearIsInvalid() throws Exception {

        mockMvc.perform(
                        get("/expenses/summary/month")
                                .param("year", "abc")
                                .param("month", "10")
                                .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Parâmetro inválido na requisição."));

        verifyNoInteractions(expenseService);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenMonthIsMissing() throws Exception {

        mockMvc.perform(
                        get("/expenses/summary/month")
                                .param("year", "2026")
                                .with(csrf())
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(expenseService);
    }
}
