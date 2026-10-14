package com.OrganizaDinheiro.OrganizaDinheiro.exception;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.ErrorResponse;
import com.OrganizaDinheiro.OrganizaDinheiro.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                exception.getMessage(),
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExpenseNotFound(
            ExpenseNotFoundException exception
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                exception.getMessage(),
                null,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                "Erro de validação",
                errors,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = "Parâmetro inválido na requisição.";

        if (exception.getRequiredType() == Category.class) {
            message = "Categoria inválida. Informe uma categoria válida.";
        }

        ErrorResponse response = new ErrorResponse(
                status.value(),
                message,
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);
    }

}