package com.khai.edu.knysh.provide_and_order_services.exception;

public class AccountTransactionNotFoundException extends RuntimeException {
    public AccountTransactionNotFoundException(String message) {
        super(message);
    }

    public AccountTransactionNotFoundException() {
    }
}
