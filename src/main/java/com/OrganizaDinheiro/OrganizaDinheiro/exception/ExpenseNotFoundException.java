package com.OrganizaDinheiro.OrganizaDinheiro.exception;

public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(int status, String message){
        super(message);
    }
}
